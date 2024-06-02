package dev.blackoutburst.game.utils

import dev.blackoutburst.game.world.World
import kotlin.math.floor

val chunkFloor = { v: Float -> (floor(v / World.CHUNK_SIZE) * World.CHUNK_SIZE).toInt() }

fun concatenateFloatArray(listOfArrays: List<FloatArray>): FloatArray {
    val totalSize = listOfArrays.sumOf { it.size }
    val result = FloatArray(totalSize)

    var offset = 0
    for (array in listOfArrays) {
        System.arraycopy(array, 0, result, offset, array.size)
        offset += array.size
    }

    return result
}

fun concatenateIntArray(listOfArrays: List<IntArray>): IntArray {
    val totalSize = listOfArrays.sumOf { it.size }
    val result = IntArray(totalSize)

    var offset = 0
    for (array in listOfArrays) {
        System.arraycopy(array, 0, result, offset, array.size)
        offset += array.size
    }

    return result
}

fun concatenateUByteArray(listOfArrays: List<ByteArray>): ByteArray {
    val totalSize = listOfArrays.sumOf { it.size }
    val result = ByteArray(totalSize)

    var offset = 0
    for (array in listOfArrays) {
        System.arraycopy(array, 0, result, offset, array.size)
        offset += array.size
    }

    return result
}