package dev.blackoutburst.game.world

import dev.blackoutburst.game.graphics.WorldBlock
import dev.blackoutburst.game.maths.Vector3f

data class Chunk(
    val position: Vector3f,
    val blocks: MutableList<WorldBlock>
)