package dev.blackoutburst.server.core.world

import dev.blackoutburst.server.maths.Vector3i
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.packets.server.S04SendChunk
import dev.blackoutburst.server.network.packets.server.S05SendMonoTypeChunk
import dev.blackoutburst.server.optimalBatchSize
import dev.blackoutburst.server.utils.io
import dev.blackoutburst.server.utils.default
import io.ktor.util.collections.ConcurrentSet
import kotlinx.coroutines.*
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

object World {

    val CHUNK_SIZE = 16

    var seed = Random.nextInt()
    val chunks = ConcurrentHashMap<String, Chunk>()

    var validIndex =  ConcurrentSet<String>()

    init {
        File("./world").mkdir()

        val seedFile = File("./world/seed.dat")
        if (seedFile.exists()) {
            seed = seedFile.readText().toInt()
        } else {
            seedFile.writeBytes(seed.toString().toByteArray())
        }
    }

    fun loadChunk(distance: Int) = runBlocking {
        val indexes = mutableSetOf<String>()
        val size = Server.clients.size

        for (i in 0 until size) {
            val client = try { Server.clients[i] } catch (ignored: Exception) { null } ?: continue
            val player = try { Server.entityManger.getEntity(client.entityId) } catch (ignored: Exception) { null } ?:continue
            val playerPosition = Chunk.getIndex(player.position.toInt())
            val renderDistance = (if (client.renderDistance < distance) client.renderDistance else distance) * 16

            val chunksToLoad = mutableListOf<Pair<Int, Vector3i>>()
            for (x in playerPosition.x - renderDistance until playerPosition.x + renderDistance step CHUNK_SIZE) {
                for (y in playerPosition.y - renderDistance until playerPosition.y + renderDistance step CHUNK_SIZE) {
                    for (z in playerPosition.z - renderDistance until playerPosition.z + renderDistance step CHUNK_SIZE) {
                        if (y < -32) continue
                        val chunkPosition = Vector3i(x, y, z)
                        chunksToLoad.add(playerPosition.distanceTo(chunkPosition) to chunkPosition)
                    }
                }
            }

            chunksToLoad.sortBy { it.first }
            val batches = chunksToLoad.chunked(optimalBatchSize(chunksToLoad.size))
            val deferredProcess = mutableListOf<Deferred<Unit>>()
            for (batch in batches) {
                deferredProcess.add(async {
                    for ((_, chunkPosition) in batch) {
                        val chunk = getChunkAt(chunkPosition.x, chunkPosition.y, chunkPosition.z)
                        indexes.add(chunk.position.toString())

                        if (!chunk.players.contains(client.entityId)) {
                            chunk.players.add(client.entityId)

                            if (!chunk.isEmpty) {
                                if (chunk.isMonoType)
                                    client.write(S05SendMonoTypeChunk(chunkPosition, chunk.blocks.first()))
                                else
                                    client.write(S04SendChunk(chunkPosition, chunk.blocks))
                            }
                        }
                    }
                })
            }
            deferredProcess.awaitAll()
        }
        validIndex = indexes
        unloadChunk()
    }


    private fun unloadChunk() = runBlocking {
        val deadIndexes = mutableSetOf<String>()

        for (chunk in chunks.values) {
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

    suspend fun updateChunk(position: Vector3i, blockType: Byte, write: Boolean = true): Chunk {
        val index = Chunk.getIndex(position)
        val chunk = chunks[index.toString()] ?: getChunkAt(index.x, index.y, index.z, true)

        val positionInChunk = Vector3i(
            if (position.x % 16 < 0) position.x % 16 + CHUNK_SIZE else position.x % 16,
            if (position.y % 16 < 0) position.y % 16 + CHUNK_SIZE else position.y % 16,
            if (position.z % 16 < 0) position.z  % 16 + CHUNK_SIZE else position.z % 16,
        )

        val blockId = chunk.xyzToIndex(positionInChunk.x, positionInChunk.y, positionInChunk.z)
        chunk.blocks[blockId] = blockType
        chunk.isEmpty = chunk._isEmpty()
        chunk.isMonoType = chunk._isMonoType()

        if (write) {
            val playerSize = chunk.players.size
            for (i in 0 until playerSize) {
                val client = Server.getClientByEntityId(chunk.players[i]) ?: continue

                if (chunk.isMonoType)
                    client.write(S05SendMonoTypeChunk(index, chunk.blocks.first()))
                else
                    client.write(S04SendChunk(index, chunk.blocks))
            }
        }
        return chunk
    }

    fun saveChunk(chunk: Chunk) {
        val file = File("./world/c_${chunk.position.x}_${chunk.position.y}_${chunk.position.z}.dat")
        file.writeBytes(chunk.blocks)
    }

    fun save() {
        chunks.values.forEach {
            File("./world/c_${it.position.x}_${it.position.y}_${it.position.z}.dat").writeBytes(it.blocks)
        }
    }

    suspend fun getChunkAt(x: Int, y: Int, z: Int, force: Boolean = false): Chunk {
        val position = Vector3i(x, y, z)

        chunks[position.toString()]?.let { return it }

        val chunkFile = File("./world/c_${x}_${y}_${z}.dat")
        if (chunkFile.exists()) {
            val chunk = Chunk(position, chunkFile.readBytes())
            if (force || !chunk.isEmpty)
                chunks[position.toString()] = chunk
            return chunk
        }

        val chunk = Chunk(position)
        chunk.fillBlocksAsync()
        if (!chunk.isEmpty) {
            chunks[position.toString()] = chunk
        }
        io { saveChunk(chunk) }

        return chunk
    }
}