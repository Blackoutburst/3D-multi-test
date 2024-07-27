package dev.blackoutburst.server.core.world

import dev.blackoutburst.server.maths.Vector3i
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.packets.server.S04SendChunk
import dev.blackoutburst.server.network.packets.server.S05SendMonoTypeChunk
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.time.Duration
import java.time.Instant
import java.util.*
import kotlin.random.Random

object World {

    val CHUNK_SIZE = 16

    var seed = Random.nextLong()
    val chunks: MutableMap<String, Chunk> = Collections.synchronizedMap(LinkedHashMap())

    init {
        File("./world").mkdir()

        val seedFile = File("./world/seed.dat")
        if (seedFile.exists()) {
            seed = seedFile.readText().toLong()
        } else {
            seedFile.writeBytes(seed.toString().toByteArray())
        }
    }

    private fun loadChunk(distance: Int) = runBlocking {
        val validIndexes = mutableListOf<String>()

        val size = Server.clients.size
        coroutineScope {
            for (i in 0 until size) {
                launch {
                    val client = try { Server.clients[i] } catch (ignored: Exception) { null } ?: return@launch
                    val player = try { Server.entityManger.getEntity(client.entityId) } catch (ignored: Exception) { null } ?: return@launch
                    val playerPosition = Chunk.getIndex(player.position.toInt())

                    for (x in playerPosition.x - distance until playerPosition.x + distance step CHUNK_SIZE) {
                    for (y in playerPosition.y - distance until playerPosition.y + distance step CHUNK_SIZE) {
                    for (z in playerPosition.z - distance until playerPosition.z + distance step CHUNK_SIZE) {
                        if (y < -32) continue
                        val chunk = getChunkAt(x, y, z)

                        validIndexes.add(chunk.position.toString())

                        if (chunk.isEmpty() || chunk.players.contains(client.entityId)) continue
                        chunk.players.add(client.entityId)

                        if (chunk.isMonoType())
                            client.write(S05SendMonoTypeChunk(Vector3i(x, y, z), chunk.blocks.first()))
                        else
                            client.write(S04SendChunk(Vector3i(x, y, z), chunk.blocks))
                    }}}
                }
            }
        }

        val chunkSize = chunks.size
        for (i in 0 until chunkSize) {
            val chunk = try { chunks.values.toList()[i] } catch (ignored: Exception) { null } ?: continue
            if (validIndexes.contains(chunk.position.toString())) continue
            if (chunk.players.isEmpty()) continue

            chunk.deletionStart = Instant.now()
            chunk.players.clear()
        }
    }

    private fun unloadChunk() = runBlocking {
        val deadIndexes = mutableListOf<String>()
        val chunkSize = chunks.size
        for (i in 0 until chunkSize) {
            val chunk = try { chunks.values.toList()[i] } catch (ignored: Exception) { null } ?: continue
            if (chunk.players.isEmpty() && Duration.between(chunk.deletionStart, Instant.now()).seconds >= 60) {
                deadIndexes.add(chunk.position.toString())
                saveChunk(chunk)
            }
        }

        deadIndexes.forEach {
            chunks.remove(it)
        }
    }

    fun update(distance: Int) {
        loadChunk(distance * 16)
        unloadChunk()
    }

    fun updateChunk(position: Vector3i, blockType: Byte, write: Boolean = true): Chunk {
        val index = Chunk.getIndex(position)
        val chunk = chunks[index.toString()] ?: getChunkAt(index.x, index.y, index.z)

        val positionInChunk = Vector3i(
            if (position.x % 16 < 0) position.x % 16 + CHUNK_SIZE else position.x % 16,
            if (position.y % 16 < 0) position.y % 16 + CHUNK_SIZE else position.y % 16,
            if (position.z % 16 < 0) position.z  % 16 + CHUNK_SIZE else position.z % 16,
        )

        val blockId = chunk.xyzToIndex(positionInChunk.x, positionInChunk.y, positionInChunk.z)
        chunk.blocks[blockId] = blockType

        if (write && chunk.isMonoType())
            Server.write(S05SendMonoTypeChunk(index, chunk.blocks.first()))
        else if (write)
            Server.write(S04SendChunk(index, chunk.blocks))

        return chunk
    }

    fun saveChunk(chunk: Chunk) {
        File("./world/c_${chunk.position.x}_${chunk.position.y}_${chunk.position.z}.dat").writeBytes(chunk.blocks.toByteArray())
    }

    fun save() {
        chunks.values.forEach {
            File("./world/c_${it.position.x}_${it.position.y}_${it.position.z}.dat").writeBytes(it.blocks.toByteArray())
        }
    }

    private fun getChunkAt(x: Int, y: Int, z: Int): Chunk {
        val position = Vector3i(x, y, z)

        chunks[position.toString()]?.let { return it }

        val chunkFile = File("./world/c_${x}_${y}_${z}.dat")
        if (chunkFile.exists()) {
            val chunk = Chunk(position, chunkFile.readBytes().toTypedArray())

            chunks[position.toString()] = chunk
            return chunk
        }

        val chunk = Chunk(position)

        if (!chunk.isEmpty())
            chunks[position.toString()] = chunk

        return chunk
    }
}