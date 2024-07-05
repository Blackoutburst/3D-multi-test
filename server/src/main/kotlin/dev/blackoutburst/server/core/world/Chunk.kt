package dev.blackoutburst.server.core.world

import dev.blackoutburst.server.maths.Vector3i
import dev.blackoutburst.server.utils.OpenSimplex2
import kotlin.math.pow

class Chunk {
    var position: Vector3i
    var blocks: Array<Byte>

    companion object {
        fun getIndex(position: Vector3i, chunkSize: Int): Vector3i {
            return Vector3i(
                (if (position.x < 0) (position.x + 1) / chunkSize - 1 else position.x / chunkSize) * chunkSize,
                (if (position.y < 0) (position.y + 1) / chunkSize - 1 else position.y / chunkSize) * chunkSize,
                (if (position.z < 0) (position.z + 1) / chunkSize - 1 else position.z / chunkSize) * chunkSize
            )
        }
    }

    constructor() {
        this.position = Vector3i()
        this.blocks = Array(4096) { BlockType.AIR.id }
    }

    constructor(position: Vector3i, blocks: Array<Byte>) {
        this.position = position
        this.blocks = blocks
    }

    constructor(position: Vector3i) {
        this.position = position
        this.blocks = Array(4096) { BlockType.AIR.id }


        for (i in 0 until 4096) {
            this.blocks[i] = getType(position.x + i % 16, position.y + (i / 16) % 16, position.z + (i / (16 * 16)) % 16).id
        }
    }

    fun isMonoType(): Boolean = this.blocks.all { it == this.blocks.first() }

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


    private fun octaveNoise2D(x: Float, z: Float, octaves: Int): Float {
        var h: Float = 0.0f

        for (i in 0..<octaves) {
            val v = OpenSimplex2.noise2(World.seed, // [-1, 1]
                        x.toDouble() * (i+1f) * 4f,
                        z.toDouble() * (i+1f) * 4f
                    )
            h += v / 2f.pow(i)
        }

        return h
    }

    private fun octaveNoise3D(x: Float, y: Float, z: Float, octaves: Int, fallOff: Float = 2f): Float {
        var h: Float = 0.0f

        for (i in 0..octaves) {
            val v = OpenSimplex2.noise3_ImproveXZ(World.seed,
                x.toDouble() * (i+1f) * 4f,
                y.toDouble() * (i+1f) * 4f,
                z.toDouble() * (i+1f) * 4f)
            h += v / fallOff.pow(i)
        }

        return h
    }

    private fun continentalnessCurve(x: Float): Float {
//        if (x > 1f) return 0.7646f
//        if (x < -1f) return -0.7646f
        return kotlin.math.sin(x * x * x * 0.6f) + 0.2f
    }

    private fun lerp(a: Float, b: Float, t: Float): Float {
        return a * (1f - t) + (b * t)
    }

    private fun mapRange(value: Float, min1: Float, max1: Float, min2: Float, max2: Float): Float {
        return min2 + (value - min1) * (max2 - min2) / (max1 - min1)
    }

    private fun getType(x: Int, y: Int, z: Int): BlockType {
        val continentalnessNoiseScaleFactor = 1f / 900f
        val continentalness = continentalnessCurve(octaveNoise2D(x * continentalnessNoiseScaleFactor, z * continentalnessNoiseScaleFactor, 3)) // 2D [-1, 1]
        val erosionNoiseScaleFactor = 1f / 1800f
        val erosion = octaveNoise2D(x * erosionNoiseScaleFactor, z * erosionNoiseScaleFactor, 2) // 2D [-1, 1]
//        val peaksAndValleys

        val densityNoiseScaleFactor = 1f / 600f
        val density = octaveNoise3D(x * densityNoiseScaleFactor, y * densityNoiseScaleFactor, z * densityNoiseScaleFactor, 5, 1.3f) // 3D [-1, 1]


        val normalizedHeight = y / 128f
        val squashing = 0.21f * (1f - mapRange(erosion, -1f, 1f, 0f, 1f)) // using erosion to affect squashing
        val fill = density * squashing > normalizedHeight - continentalness * 0.4f

        if (fill) {
            if (y > 0f) return BlockType.GRASS
            if (y == 0) return BlockType.OAK_LOG
            return BlockType.STONE
        }
        return BlockType.AIR
    }


}