package dev.blackoutburst.server.core.world

import dev.blackoutburst.server.maths.Vector3i
import dev.blackoutburst.server.optimalBatchSize
import dev.blackoutburst.server.utils.FastNoiseLite
import kotlinx.coroutines.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.round
import kotlin.random.Random

class Chunk {
    var position: Vector3i
    var blocks = ByteArray(4096)
    var players: MutableList<Int>
    var isEmpty: Boolean = false
    var isMonoType: Boolean = false

    companion object {
        val base = FastNoiseLite()
        val mountain = FastNoiseLite()
        val spike = FastNoiseLite()
        val elevation = FastNoiseLite()

        const val SCALE_BASE = 0.25f
        const val SCALE_MOUNTAIN = 0.2f
        const val SCALE_SPIKE = 0.4f
        const val SCALE_ELEVATION = 0.005f

        init {
            base.SetSeed(World.seed)

            base.SetNoiseType(FastNoiseLite.NoiseType.Perlin)

            base.SetFrequency(0.01f)

            base.SetFractalLacunarity(2f)
            base.SetFractalGain(0.5f)
            base.SetFractalOctaves(5)
            base.SetFractalType(FastNoiseLite.FractalType.FBm)

            mountain.SetSeed(World.seed)

            mountain.SetNoiseType(FastNoiseLite.NoiseType.Perlin)

            mountain.SetFrequency(0.01f)

            mountain.SetFractalLacunarity(2f)
            mountain.SetFractalGain(0.5f)
            mountain.SetFractalOctaves(2)
            mountain.SetFractalType(FastNoiseLite.FractalType.FBm)

            spike.SetSeed(World.seed)

            spike.SetNoiseType(FastNoiseLite.NoiseType.Perlin)

            spike.SetFrequency(0.01f)

            spike.SetFractalLacunarity(4f)
            spike.SetFractalGain(0.5f)
            spike.SetFractalOctaves(3)
            spike.SetFractalType(FastNoiseLite.FractalType.FBm)

            elevation.SetSeed(World.seed)

            elevation.SetNoiseType(FastNoiseLite.NoiseType.Perlin)

            elevation.SetFrequency(0.01f)

            elevation.SetFractalLacunarity(2f)
            elevation.SetFractalGain(0.5f)
            elevation.SetFractalOctaves(1)
            elevation.SetFractalType(FastNoiseLite.FractalType.FBm)
        }

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
        this.blocks = ByteArray(4096)
    }

    constructor(position: Vector3i, blocks: ByteArray) {
        this.players = mutableListOf()
        this.position = position
        this.blocks = blocks
        isEmpty = _isEmpty()
        isMonoType = _isMonoType()
    }

    constructor(position: Vector3i) {
        this.players = mutableListOf()
        this.position = position
        this.blocks = ByteArray(4096)
    }

    suspend fun fillBlocksAsync() = withContext(Dispatchers.Default) {
        val blockIndices = blocks.indices.toList()
        val chunks = blockIndices.chunked(optimalBatchSize(blocks.size))

        val deferred = chunks.map { chunk ->
            async {
                for (i in chunk) {
                    val xOffset = i % 16
                    val yOffset = (i / 16) % 16
                    val zOffset = i / (16 * 16)

                    val x = position.x + xOffset
                    val y = position.y + yOffset
                    val z = position.z + zOffset

                    blocks[i] = getType(x, y, z).id
                }
            }
        }

        deferred.awaitAll()
        isEmpty = _isEmpty()
        isMonoType = _isMonoType()
    }


    fun _isEmpty(): Boolean {
        if (this.blocks.isEmpty()) return true

        for (i in 0 until 4096) {
            if (this.blocks[i] != BlockType.AIR.id) return false
        }

        return true
    }

    fun _isMonoType(): Boolean {
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

    private fun easeInOutQuint(value: Float): Float {
        val v = if (value < 0.5f) 16 * value * value * value * value * value
        else 1 - (-2 * value + 2).pow(5) / 2
        return v
    }

    suspend fun genTree() {
        var index = -1
        for (block in this.blocks) {
            index++
            if (block != BlockType.GRASS.id) continue
            if (Random.nextInt(200) != 0) continue
            val position = indexToXYZ(index)
            val treeHeight = Random.nextInt(3) + 3

            for (x in position.x -2 .. position.x + 2) {
                for (z in position.z - 2..position.z + 2) {
                    for (y in position.y .. position.y + 1) {
                        World.updateChunk(Vector3i(x, treeHeight - 1 + y, z), BlockType.OAK_LEAVES.id, false)
                    }
                }
            }

            for (x in position.x -1 .. position.x + 1) {
                for (z in position.z - 1..position.z + 1) {
                    for (y in position.y .. position.y + 1) {
                        World.updateChunk(Vector3i(x, treeHeight + 1 + y, z), BlockType.OAK_LEAVES.id, false)
                    }
                }
            }

            for (y in position.y + 1 .. position.y + treeHeight) {
                World.updateChunk(Vector3i(position.x, y, position.z), BlockType.OAK_LOG.id, false)
            }

        }
    }

    private fun getType(x: Int, y: Int, z: Int): BlockType {
        val baseNoise = base.GetNoise(x * SCALE_BASE, z * SCALE_BASE)
        val mountainNoise = mountain.GetNoise(x * SCALE_MOUNTAIN, z * SCALE_MOUNTAIN)
        val spikeNoise = spike.GetNoise(x * SCALE_SPIKE, z * SCALE_SPIKE)
        val elevationNoise = elevation.GetNoise(x * SCALE_ELEVATION, z * SCALE_ELEVATION)

        val height = round(
            baseNoise * 50 +
                abs(easeInOutQuint(mountainNoise)) * 70 +
                abs(easeInOutQuint(spikeNoise)) * 50 +
                elevationNoise * 500
        ).toInt()

        return if ((y == height || y == height -1 || y == height -2 || y == height -3) && height > 50) BlockType.SNOW
        else if ((y == height || y == height -1 || y == height -2 || y == height -3) && height > 30) BlockType.STONE
        else if (y == height && height < 2) BlockType.SAND
        else if (y == height) BlockType.GRASS
        else if (y < height - 3) BlockType.STONE
        else if (y in (height + 1)..-1) BlockType.WATER
        else if (y > height) BlockType.AIR
        else BlockType.DIRT
    }

}