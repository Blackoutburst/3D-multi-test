package dev.blackoutburst.server.core.world

import dev.blackoutburst.game.maths.Vector3i

data class Block(
    var type: BlockType,
    val position: Vector3i
)