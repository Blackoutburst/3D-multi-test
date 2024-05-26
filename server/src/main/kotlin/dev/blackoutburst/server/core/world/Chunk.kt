package dev.blackoutburst.server.core.world

import dev.blackoutburst.game.maths.Vector3i
import dev.blackoutburst.server.utils.OpenSimplex2

class Chunk(
    val position: Vector3i,
    val blocks: Array<Byte> = Array(4096) { BlockType.AIR.id }
) {

    init {
        for (i in 0 until 4096) {
            blocks[i] = getType(position.x + i % 16, position.y + (i / 16) % 16, position.z + (i / (16 * 16)) % 16).id
        }
    }

    fun xyzToIndex(x: Int, y: Int, z: Int): Int = x + 16 * (y + 16 * z)

    private fun getType(x:Int, y: Int, z: Int): BlockType {
        val height = (OpenSimplex2.noise2(World.seed, x / 100.0, z / 100.0) * 7).toInt() + 10

        return if (y > height) BlockType.AIR
        else if (y == height) BlockType.GRASS
        else if (y < height - 3) BlockType.STONE
        else BlockType.DIRT
    }
}