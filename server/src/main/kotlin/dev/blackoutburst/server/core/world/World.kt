package dev.blackoutburst.server.core.world

import dev.blackoutburst.server.core.entity.EntityPlayer
import dev.blackoutburst.server.maths.Vector3i
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.packets.server.S04SendChunk
import dev.blackoutburst.server.network.packets.server.S05SendMonoTypeChunk
import dev.blackoutburst.server.utils.OpenSimplex2
import dev.blackoutburst.server.utils.chunkFloor
import dev.blackoutburst.server.utils.default
import java.io.File
import java.util.*
import kotlin.random.Random

object World {

    val CHUNK_SIZE = 16

    val seed = Random.nextLong()
    val chunks: MutableMap<String, Chunk> = Collections.synchronizedMap(LinkedHashMap())

    private fun loadChunk(distance: Int) {
        val size = Server.clients.size
        for (clientId in 0 until size) {
            val client = try { Server.clients[clientId] } catch (ignored: Exception) { null } ?: break
            val player = Server.entityManger.entities.find { it.id == client.entityId }
            if (player == null) continue

            val pX = chunkFloor(player.position.x)
            val pY = chunkFloor(player.position.y)
            val pZ = chunkFloor(player.position.z)

            for (x in pX - distance until pX + distance) {
                for (y in pY - distance until pY + distance) {
                    for (z in pZ - distance until pZ + distance) {
                        if (y < -32 || y > 256) continue

                        val index = (Vector3i(chunkFloor(x.toFloat()), chunkFloor(y.toFloat()), chunkFloor(z.toFloat())))
                        if (chunks[index.toString()] == null) {
                            val chunk = addChunk(
                                chunkFloor(x.toFloat()),
                                chunkFloor(y.toFloat()),
                                chunkFloor(z.toFloat())
                            )
                            
                            if (chunk.isMonoType()) {
                                if (chunk.blocks.first().toInt() == 0) continue
                                Server.write(S05SendMonoTypeChunk(chunk.position, chunk.blocks.first()))
                            } else {
                                Server.write(S04SendChunk(chunk.position, chunk.blocks))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun unloadChunk(distance: Int) {
        val indices = mutableListOf<String>()

        val size = chunks.size
        chunk@for (chunkId in 0 until size) {
            val chunk = try { chunks.values.toList()[chunkId] } catch (ignored: Exception) { null } ?: break
            val entitySize = Server.entityManger.entities.size
            for (entityId in 0 until entitySize) {
                val player = try { Server.entityManger.entities[entityId] } catch (ignored: Exception) { null } ?: break
                if (player !is EntityPlayer) continue@chunk

                val pX = chunkFloor(player.position.x)
                val pY = chunkFloor(player.position.y)
                val pZ = chunkFloor(player.position.z)

                if (chunk.position.x in pX - distance until pX + distance &&
                    chunk.position.y in pY - distance until pY + distance &&
                    chunk.position.z in pZ - distance until pZ + distance) {
                    indices.add(chunk.position.toString())
                }
            }
        }

        val keys = chunks.keys.filter { !indices.contains(it) }
        val keySize = keys.size
        chunk@for (keyId in 0 until keySize) {
            val key = keys[keyId]
            val chunk = chunks[key]
            chunk?.let { saveChunk(it) }
            chunks.remove(key)
        }
    }

    fun update() {
        val distance = 16 * 5

        loadChunk(distance)
        unloadChunk(distance)
    }

    fun generate(size: Int, height: Int) {
        println("Generating world")
        for (x in -size until size) {
            for (y in -height until height) {
                for (z in -size until size) {
                    addChunk(x * CHUNK_SIZE, y * CHUNK_SIZE, z * CHUNK_SIZE)
                }
            }
        }
        for (x in -size * CHUNK_SIZE until size * CHUNK_SIZE) {
            for (z in -size * CHUNK_SIZE until size * CHUNK_SIZE) {
                addTree(x, z)
            }
        }

        println("World generated")
    }

    private fun addTree(x: Int, z: Int) {
        if (Random.nextInt(300) != 0) return
        val y = (OpenSimplex2.noise2(seed, x / 200.0, z / 200.0) * 20).toInt() + 10

        for (yo in 0..1) {
            for (xo in -2..2) {
                for (zo in -2..2) {
                    updateChunk(Vector3i(x + xo, y + 3 + yo, z + zo), BlockType.OAK_LEAVES.id, false)
                }
            }
        }

        for (xo in -1..1) {
            for (zo in -1..1) {
                updateChunk(Vector3i(x + xo, y + 5, z + zo), BlockType.OAK_LEAVES.id, true)
            }
        }

        for (i in 0..4) {
            updateChunk(Vector3i(x, y + i, z), BlockType.OAK_LOG.id, true)
        }
    }

    fun updateChunk(position: Vector3i, blockType: Byte, write: Boolean = true): Chunk? {
        val index = (Vector3i(chunkFloor(position.x.toFloat()), chunkFloor(position.y.toFloat()), chunkFloor(position.z.toFloat())))

        val chunk = chunks[index.toString()] ?: return null

        val positionAsInt = Vector3i(
            if (position.x % 16 < 0) position.x % 16 + CHUNK_SIZE else position.x % 16,
            if (position.y % 16 < 0) position.y % 16 + CHUNK_SIZE else position.y % 16,
            if (position.z % 16 < 0) position.z  % 16 + CHUNK_SIZE else position.z % 16,
        )

        val blockId = chunk.xyzToIndex(positionAsInt.x, positionAsInt.y, positionAsInt.z)
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

    private fun addChunk(x: Int, y: Int, z: Int): Chunk {
        val position = Vector3i(x, y, z)
        val chunkFile = File("./world/c_${x}_${y}_${z}.dat")
        if (chunkFile.exists()) {
            val chunk = Chunk(position, chunkFile.readBytes().toTypedArray())

            chunks[position.toString()] = chunk
            return chunk
        }

        val chunk = Chunk(position)

        chunks[position.toString()] = chunk
        return chunk
    }

    fun getBlockAt(position: Vector3i): Block? {
        val chunkPosition = Chunk.getIndex(position, CHUNK_SIZE)
        val chunk = chunks[chunkPosition.toString()] ?: return null
        val positionAsInt = Vector3i(
            ((position.x - chunk.position.x) % CHUNK_SIZE + CHUNK_SIZE) % CHUNK_SIZE,
            ((position.y - chunk.position.y) % CHUNK_SIZE + CHUNK_SIZE) % CHUNK_SIZE,
            ((position.z - chunk.position.z) % CHUNK_SIZE + CHUNK_SIZE) % CHUNK_SIZE
        )

        val blockId = chunk.xyzToIndex(positionAsInt.x, positionAsInt.y, positionAsInt.z)
        val blockType = chunk.blocks[blockId]
        if (blockType == BlockType.AIR.id) return null

        return Block(BlockType.getByID(chunk.blocks[blockId]), chunk.indexToXYZ(blockId))
    }
}