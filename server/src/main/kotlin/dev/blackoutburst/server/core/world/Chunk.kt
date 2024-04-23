package dev.blackoutburst.server.core.world

import dev.blackoutburst.game.maths.Vector3i
import dev.blackoutburst.server.utils.OpenSimplex2

class Chunk(
    val position: Vector3i,
    val blocks: MutableList<Block> = mutableListOf()
) {

    init {
        for (x in 0 until World.CHUNK_SIZE) {
            for (y in 0 until World.CHUNK_SIZE) {
                for (z in 0 until World.CHUNK_SIZE) {
                    blocks.add(Block(
                        type = getType(position.x + x, position.y + y, position.z + z),
                        position = Vector3i(
                            position.x + x,
                            position.y + y,
                            position.z + z
                        ),
                    ))
                }
            }
        }
    }

    private fun getType(x:Int, y: Int, z: Int): BlockType {
        val height = (OpenSimplex2.noise2(World.seed, x / 100.0, z / 100.0) * 7).toInt() + 10

        return if (y > height) BlockType.AIR else BlockType.GRASS
    }
}