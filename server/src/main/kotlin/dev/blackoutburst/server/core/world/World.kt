package dev.blackoutburst.server.core.world

import dev.blackoutburst.server.maths.Vector3i
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.packets.server.S04SendChunk
import dev.blackoutburst.server.network.packets.server.S05SendMonoTypeChunk
import dev.blackoutburst.server.utils.io
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.*
import kotlin.random.Random

object World {

    val CHUNK_SIZE = 16

    var seed = Random.nextLong()
    val chunks: MutableMap<String, Chunk> = Collections.synchronizedMap(LinkedHashMap())

    val chunkFiles = mutableMapOf<String, File>()

    var validIndex = setOf<String>()

    init {
        File("./world").mkdir()

        val worldFolder = File("./world")
        if (worldFolder.exists()) {
            worldFolder.listFiles()?.forEach {
                if (it.nameWithoutExtension.startsWith("c_"))
                    chunkFiles[it.nameWithoutExtension.replace("c_", "")] = it
            }
        }

        val seedFile = File("./world/seed.dat")
        if (seedFile.exists()) {
            seed = seedFile.readText().toLong()
        } else {
            seedFile.writeBytes(seed.toString().toByteArray())
        }
    }

    fun loadChunk(distance: Int) = runBlocking {
        val indexes = mutableSetOf<String>()

        val size = Server.clients.size
        coroutineScope {
            for (i in 0 until size) {
                val client = try { Server.clients[i] } catch (ignored: Exception) { null } ?: continue
                val player = try { Server.entityManger.getEntity(client.entityId) } catch (ignored: Exception) { null } ?: continue
                val playerPosition = Chunk.getIndex(player.position.toInt())
                val renderDistance = (if (client.renderDistance < distance) client.renderDistance else distance) * 16

                for (x in playerPosition.x - renderDistance until playerPosition.x + renderDistance step CHUNK_SIZE) {
                    for (y in playerPosition.y - renderDistance until playerPosition.y + renderDistance step CHUNK_SIZE) {
                        for (z in playerPosition.z - renderDistance until playerPosition.z + renderDistance step CHUNK_SIZE) {
                            launch {
                                if (y < -32) return@launch
                                val chunk = getChunkAt(x, y, z)
                                indexes.add(chunk.position.toString())

                                if (chunk.players.contains(client.entityId)) return@launch
                                chunk.players.add(client.entityId)

                                if (chunk.isEmpty()) return@launch

                                if (chunk.isMonoType())
                                    client.write(S05SendMonoTypeChunk(Vector3i(x, y, z), chunk.blocks.first()))
                                else
                                    client.write(S04SendChunk(Vector3i(x, y, z), chunk.blocks))
                            }
                        }
                    }
                }
            }
        }
        validIndex = indexes
    }

    fun unloadChunk() = runBlocking {
        val deadIndexes = mutableSetOf<String>()
        val chunkSize = chunks.size
        val chunkList = chunks.values.toList()

        for (i in 0 until chunkSize) {
            val chunk = try { chunkList[i] } catch (ignored: Exception) { null } ?: continue
            if (!validIndex.contains(chunk.position.toString())) {
                chunk.players.clear()
                deadIndexes.add(chunk.position.toString())
                io { saveChunk(chunk) }
            }
        }

        deadIndexes.forEach {
            chunks.remove(it)
        }
    }

    fun updateChunk(position: Vector3i, blockType: Byte, write: Boolean = true): Chunk {
        val index = Chunk.getIndex(position)
        val chunk = chunks[index.toString()] ?: getChunkAt(index.x, index.y, index.z, true)

        val positionInChunk = Vector3i(
            if (position.x % 16 < 0) position.x % 16 + CHUNK_SIZE else position.x % 16,
            if (position.y % 16 < 0) position.y % 16 + CHUNK_SIZE else position.y % 16,
            if (position.z % 16 < 0) position.z  % 16 + CHUNK_SIZE else position.z % 16,
        )

        val blockId = chunk.xyzToIndex(positionInChunk.x, positionInChunk.y, positionInChunk.z)
        chunk.blocks[blockId] = blockType

        if (write) {
            val playerSize = chunk.players.size
            for (i in 0 until playerSize) {
                val client = Server.getClientByEntityId(chunk.players[i]) ?: continue

                if (chunk.isMonoType())
                    client.write(S05SendMonoTypeChunk(index, chunk.blocks.first()))
                else
                    client.write(S04SendChunk(index, chunk.blocks))
            }
        }

        return chunk
    }

    fun saveChunk(chunk: Chunk) {
        val file = File("./world/c_${chunk.position.x}_${chunk.position.y}_${chunk.position.z}.dat")
        file.writeBytes(chunk.blocks.toByteArray())

        chunkFiles["${chunk.position.x}_${chunk.position.y}_${chunk.position.z}"] = file
    }

    fun save() {
        chunks.values.forEach {
            File("./world/c_${it.position.x}_${it.position.y}_${it.position.z}.dat").writeBytes(it.blocks.toByteArray())
        }
    }

    private fun getChunkAt(x: Int, y: Int, z: Int, force: Boolean = false): Chunk {
        val position = Vector3i(x, y, z)

        chunks[position.toString()]?.let { return it }

        val chunkFile = chunkFiles["${x}_${y}_${z}"]
        if (chunkFile != null) {
            val chunk = Chunk(position, chunkFile.readBytes().toTypedArray())
            if (force || !chunk.isEmpty())
                chunks[position.toString()] = chunk
            return chunk
        }

        val chunk = Chunk(position)
        if (force || !chunk.isEmpty())
            chunks[position.toString()] = chunk

        return chunk
    }
}