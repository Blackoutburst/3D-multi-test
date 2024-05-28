package dev.blackoutburst.server.core.world

import dev.blackoutburst.game.maths.Vector3i
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.packets.server.S04SendChunk
import dev.blackoutburst.server.utils.chunkFloor
import kotlin.random.Random

object World {

    val CHUNK_SIZE = 16

    val seed = Random.nextLong()
    val chunks = mutableMapOf<String, Chunk>()

    fun generate(size: Int, height: Int) {
        for (x in -size until size)
            for (y in -height until height)
                for (z in -size until size)
                    addChunk(x * CHUNK_SIZE, y * CHUNK_SIZE, z * CHUNK_SIZE)

    }

    fun updateChunk(position: Vector3i, blockType: Byte) {
        val index = (Vector3i(chunkFloor(position.x.toFloat()), chunkFloor(position.y.toFloat()), chunkFloor(position.z.toFloat())))

        val chunk = chunks[index.toString()] ?: return

        val positionAsInt = Vector3i(
            if (position.x % 16 < 0) position.x % 16 + CHUNK_SIZE else position.x % 16,
            if (position.y % 16 < 0) position.y % 16 + CHUNK_SIZE else position.y % 16,
            if (position.z % 16 < 0) position.z  % 16 + CHUNK_SIZE else position.z % 16,
        )

        val blockId = chunk.xyzToIndex(positionAsInt.x, positionAsInt.y, positionAsInt.z)
        chunk.blocks[blockId] = blockType

        Server.write(S04SendChunk(index, chunk.blocks))
    }

    private fun addChunk(x: Int, y: Int, z: Int) {
        val position = Vector3i(x, y, z)

        chunks[position.toString()] = Chunk(position)
    }
}