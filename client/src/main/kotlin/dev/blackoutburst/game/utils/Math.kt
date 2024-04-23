package dev.blackoutburst.game.utils

import dev.blackoutburst.game.world.World
import kotlin.math.floor

val chunkFloor = { v: Float -> (floor(v / World.CHUNK_SIZE) * World.CHUNK_SIZE).toInt() }