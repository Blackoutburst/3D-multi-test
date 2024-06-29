package dev.blackoutburst.server.core.world

import dev.blackoutburst.server.maths.Vector3i
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.packets.server.S04SendChunk
import dev.blackoutburst.server.network.packets.server.S05SendMonoTypeChunk
import dev.blackoutburst.server.utils.OpenSimplex2
import dev.blackoutburst.server.utils.chunkFloor
import java.util.*
import kotlin.collections.LinkedHashSet
import kotlin.random.Random

object World {

    val CHUNK_SIZE = 16

    val seed = Random.nextLong()
    val chunks: MutableMap<String, Chunk> = Collections.synchronizedMap(LinkedHashMap())

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
                updateChunk(Vector3i(x + xo, y + 5, z + zo), BlockType.OAK_LEAVES.id, false)
            }
        }

        for (i in 0..4) {
            updateChunk(Vector3i(x, y + i, z), BlockType.OAK_LOG.id, false)
        }
    }

    fun updateChunk(position: Vector3i, blockType: Byte, write: Boolean = true): Chunk? {
        val index = (Vector3i(chunkFloor(position.x.toFloat()), chunkFloor(position.y.toFloat()), chunkFloor(position.z.toFloat())))

        val chunk = synchronized(chunks) { chunks[index.toString()] } ?: return null

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

    private fun addChunk(x: Int, y: Int, z: Int) {
        val position = Vector3i(x, y, z)

        synchronized(chunks) { chunks[position.toString()] = Chunk(position) }
    }

    fun getBlockAt(position: Vector3i): Block? {
        val chunkPosition = Chunk.getIndex(position, CHUNK_SIZE)
        val chunk = synchronized(chunks) { chunks[chunkPosition.toString()] } ?: return null
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