package dev.blackoutburst.server.core.world

import dev.blackoutburst.game.maths.Vector3f

data class Block(
    val type: BlockType,
    val position: Vector3f
)