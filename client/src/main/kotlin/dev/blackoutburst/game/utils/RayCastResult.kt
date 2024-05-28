package dev.blackoutburst.game.utils

import dev.blackoutburst.game.maths.Vector3i
import dev.blackoutburst.game.world.Block

data class RayCastResult(val block: Block?, val face: Vector3i?)