package dev.blackoutburst.server.core.world

import dev.blackoutburst.game.maths.Vector3i

class Chunk(
    val position: Vector3i,
    val blocks: MutableList<Block> = mutableListOf()
) {
    init {
        for (x in 0 until World.CHUNK_SIZE) {
            for (y in 0 until World.CHUNK_SIZE) {
                for (z in 0 until World.CHUNK_SIZE) {
                    blocks.add(Block(
                        type = BlockType.GRASS,
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
}