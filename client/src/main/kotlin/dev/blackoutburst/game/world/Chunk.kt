package dev.blackoutburst.game.world

import dev.blackoutburst.game.graphics.WorldBlock
import dev.blackoutburst.game.maths.Vector3i

class Chunk(
    val position: Vector3i,
    val blocks: MutableList<WorldBlock>
) {
    fun getSolidBlock() = blocks.filter { it.type != BlockType.AIR }
}