package dev.blackoutburst.server.utils

import dev.blackoutburst.server.core.world.World
import kotlin.math.floor

val chunkFloor = { v: Float -> (floor(v / World.CHUNK_SIZE) * World.CHUNK_SIZE).toInt() }
