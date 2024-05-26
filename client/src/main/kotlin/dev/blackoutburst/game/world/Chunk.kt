package dev.blackoutburst.game.world

import dev.blackoutburst.game.graphics.WorldBlock
import dev.blackoutburst.game.maths.Vector3i

class Chunk(
    val position: Vector3i,
    val blocks: Array<Byte> = Array(4096) { BlockType.AIR.id }
) {

    companion object {
        fun getIndex(position: Vector3i, chunkSize: Int): Vector3i {
            return Vector3i(
                (if (position.x < 0) (position.x + 1) / chunkSize - 1 else position.x / chunkSize) * chunkSize,
                (if (position.y < 0) (position.y + 1) / chunkSize - 1 else position.y / chunkSize) * chunkSize,
                (if (position.z < 0) (position.z + 1) / chunkSize - 1 else position.z / chunkSize) * chunkSize
            )
        }
    }

    fun blockAt(x: Int, y: Int, z: Int): Byte {
        for (i in 0 until 4096) {
            if (x == i % 16 && y == (i / 16) % 16 && z == (i / (16 * 16)) % 16)
                return blocks[i]
        }
        return BlockType.AIR.id
    }

    fun xyzToIndex(x: Int, y: Int, z: Int): Int {
        for (i in 0 until 4096) {
            if (x == i % 16 && y == (i / 16) % 16 && z == (i / (16 * 16)) % 16)
                return i
        }
        return 0
    }

    fun indexToXYZ(index: Int): Vector3i = Vector3i(index % 16, (index / 16) % 16, (index / (16 * 16)) % 16) + this.position
}