package dev.blackoutburst.server.core.world

import dev.blackoutburst.server.maths.Vector3i

data class Block(
    var type: BlockType,
    val position: Vector3i
)