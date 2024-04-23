package dev.blackoutburst.server.core.world

import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.server.utils.OpenSimplex2

class Chunk(
    val position: Vector3f,
    val blocks: MutableList<Block> = mutableListOf()
) {
    init {
        for (x in position.x.toInt() until (position.x.toInt() + World.CHUNK_SIZE))
            for (y in position.y.toInt() until (position.y.toInt() + World.CHUNK_SIZE))
                for (z in position.z.toInt() until (position.z.toInt() + World.CHUNK_SIZE))
                    addBlock(x, y, z)
    }

    private fun addBlock(x: Int, y: Int, z: Int) {
        blocks.add(
            Block(
                type = BlockType.GRASS,
                position = Vector3f(
                    x = x.toFloat(),
                    y = y + (OpenSimplex2.noise2(World.seed, x / 100.0, z / 100.0) * 7).toInt().toFloat() - 10,
                    z = z.toFloat()
                ),
            )
        )
    }
}