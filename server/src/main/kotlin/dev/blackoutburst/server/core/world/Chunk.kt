package dev.blackoutburst.server.core.world

import dev.blackoutburst.server.maths.Vector3i
import dev.blackoutburst.server.utils.OpenSimplex2
import java.time.Instant

class Chunk {
    var position: Vector3i
    var blocks: Array<Byte>
    var players: MutableList<Int>
    var deletionStart = Instant.now()

    companion object {
        fun getIndex(position: Vector3i): Vector3i {
            return Vector3i(
                (if (position.x < 0) (position.x + 1) / World.CHUNK_SIZE - 1 else position.x / World.CHUNK_SIZE) * World.CHUNK_SIZE,
                (if (position.y < 0) (position.y + 1) / World.CHUNK_SIZE - 1 else position.y / World.CHUNK_SIZE) * World.CHUNK_SIZE,
                (if (position.z < 0) (position.z + 1) / World.CHUNK_SIZE - 1 else position.z / World.CHUNK_SIZE) * World.CHUNK_SIZE
            )
        }
    }

    constructor() {
        this.players = mutableListOf()
        this.position = Vector3i()
        this.blocks = Array(4096) { BlockType.AIR.id }
    }

    constructor(position: Vector3i, blocks: Array<Byte>) {
        this.players = mutableListOf()
        this.position = position
        this.blocks = blocks
    }

    constructor(position: Vector3i) {
        this.players = mutableListOf()
        this.position = position
        this.blocks = Array(4096) { BlockType.AIR.id }


        for (i in 0 until 4096) {
            this.blocks[i] = getType(position.x + i % 16, position.y + (i / 16) % 16, position.z + (i / (16 * 16)) % 16).id
        }
    }

    fun isEmpty(): Boolean {
        if (this.blocks.isEmpty()) return true

        for (i in 1 until 4096) {
            if (this.blocks[i] != BlockType.AIR.id) return false
        }

        return true
    }

    fun isMonoType(): Boolean {
        var previous = this.blocks[0]
        for (i in 1 until 4096) {
            val current = this.blocks[i]
            if (previous != current) return false
            previous = current
        }
        return true
    }

    fun xyzToIndex(x: Int, y: Int, z: Int): Int = x + 16 * (y + 16 * z)

    fun indexToXYZ(index: Int): Vector3i = Vector3i(index % 16, (index / 16) % 16, (index / (16 * 16)) % 16) + this.position

    private fun getType(x:Int, y: Int, z: Int): BlockType {
        val height = (OpenSimplex2.noise2(World.seed, x / 200.0, z / 200.0) * 20).toInt() + 10

        return if (y == height) BlockType.GRASS
        else if (y < height - 3) BlockType.STONE
        else if (y > height) BlockType.AIR
        else BlockType.DIRT
    }

}