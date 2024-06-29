package dev.blackoutburst.game.utils

import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.game.world.World
import java.lang.Math.pow
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

val chunkFloor = { v: Float -> (floor(v / World.CHUNK_SIZE) * World.CHUNK_SIZE).toInt() }

fun distance(p1: Vector2f, p2: Vector2f): Double =
    (p2.x.toDouble() - p1.x.toDouble()).pow(2.0) +
    (p2.y.toDouble() - p1.y.toDouble()).pow(2.0)
