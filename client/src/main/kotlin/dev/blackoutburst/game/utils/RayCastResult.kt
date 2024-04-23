package dev.blackoutburst.game.utils

import dev.blackoutburst.game.graphics.WorldBlock
import dev.blackoutburst.game.maths.Vector3i

data class RayCastResult(val block: WorldBlock?, val face: Vector3i?)