package dev.blackoutburst.server.core.world

import dev.blackoutburst.game.maths.Vector3i
import dev.blackoutburst.server.utils.OpenSimplex2

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

    init {
        for (i in 0 until 4096) {
            blocks[i] = getType(position.x + i % 16, position.y + (i / 16) % 16, position.z + (i / (16 * 16)) % 16).id
        }
    }

    fun isVisible(): Boolean {
        val top = World.chunks[Vector3i(this.position.x, this.position.y + 16, this.position.z).toString()]
        val bottom = World.chunks[Vector3i(this.position.x, this.position.y - 16, this.position.z).toString()]
        val back = World.chunks[Vector3i(this.position.x, this.position.y, this.position.z + 16).toString()]
        val front = World.chunks[Vector3i(this.position.x, this.position.y, this.position.z - 16).toString()]
        val right = World.chunks[Vector3i(this.position.x + 16, this.position.y, this.position.z).toString()]
        val left = World.chunks[Vector3i(this.position.x - 16, this.position.y, this.position.z).toString()]
        if (top == null || bottom == null || back == null || front == null || right == null || left == null) return true

        for (x in 0 until 16) {
            for (z in 0 until 16) {
                val t = World.getBlockAt(Vector3i(this.position.x + x, this.position.y - 1, this.position.z + z))
                if (t == null || t.type == BlockType.AIR) return true
                val b = World.getBlockAt(Vector3i(this.position.x + x, this.position.y + 16, this.position.z + z))
                if (b == null || b.type == BlockType.AIR) return true
            }
        }

        for (x in 0 until 16) {
            for (y in 0 until 16) {
                val b = World.getBlockAt(Vector3i(this.position.x + x, this.position.y + y, this.position.z + 16))
                if (b == null || b.type == BlockType.AIR) return true
                val f = World.getBlockAt(Vector3i(this.position.x + x, this.position.y + y, this.position.z - 1))
                if (f == null || f.type == BlockType.AIR) return true
            }
        }

        for (y in 0 until 16) {
            for (z in 0 until 16) {
                val l = World.getBlockAt(Vector3i(this.position.x + 16, this.position.y + y, this.position.z + z))
                if (l == null || l.type == BlockType.AIR) return true
                val r = World.getBlockAt(Vector3i(this.position.x - 1, this.position.y + y, this.position.z + z))
                if (r == null || r.type == BlockType.AIR) return true
            }
        }

        return false
    }

    fun blockAt(chunk: Chunk, x: Int, y: Int, z: Int): Byte = chunk.blocks[xyzToIndex(x, y, z)]

    fun xyzToIndex(x: Int, y: Int, z: Int): Int = x + 16 * (y + 16 * z)

    fun indexToXYZ(index: Int): Vector3i = Vector3i(index % 16, (index / 16) % 16, (index / (16 * 16)) % 16) + this.position

    fun isWithinBounds(x: Int, y: Int, z: Int): Boolean = x in 0 until 16 && y in 0 until 16 && z in 0 until 16

    fun isAir(x: Int, y: Int, z: Int): Boolean {
        return if (isWithinBounds(x, y, z)) {
            val type = BlockType.getByID(blockAt(this, x, y, z))

            type == BlockType.AIR || type.transparent
        } else {
            val type = World.getBlockAt(Vector3i(x + this.position.x, y + this.position.y, z + this.position.z))?.type
            return if (type == null) true
            else type == BlockType.AIR || type.transparent
        }
    }


    private fun getType(x:Int, y: Int, z: Int): BlockType {
        val height = (OpenSimplex2.noise2(World.seed, x / 100.0, z / 100.0) * 7).toInt() + 10

        return if (y == height) BlockType.GRASS
        else if (y < height - 3) BlockType.STONE
        else if (y > height) BlockType.AIR
        else BlockType.DIRT
    }

}