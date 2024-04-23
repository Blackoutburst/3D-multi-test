package dev.blackoutburst.server.core.world

import dev.blackoutburst.game.maths.Vector3f

object World {

    val WORLD_SIZE = 2
    val CHUNK_SIZE = 16

    val seed = 10L
    val chunks = mutableListOf<Chunk>()

    fun generate() {
        for (x in 0 until WORLD_SIZE)
            for (y in 0 until WORLD_SIZE)
                for (z in 0 until WORLD_SIZE)
                    addChunk(x * 16, y * 16, z * 16)
    }

    private fun addChunk(x: Int, y: Int, z: Int) {
        chunks.add(
            Chunk(
                Vector3f(
                    x = x.toFloat(),
                    y = y.toFloat(),
                    z = z.toFloat()
                ),
            )
        )
    }
}