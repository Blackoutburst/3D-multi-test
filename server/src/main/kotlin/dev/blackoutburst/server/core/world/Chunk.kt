package dev.blackoutburst.server.core.world

import dev.blackoutburst.server.maths.Vector3i
import dev.blackoutburst.server.utils.FastNoiseLite
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.round


class Chunk {
    var position: Vector3i
    var blocks: Array<Byte>
    var players: MutableList<Int>

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

        val logs = mutableListOf<Vector3i>()

        for (i in 0 until 4096) {
            val type = getType(position.x + i % 16, position.y + (i / 16) % 16, position.z + (i / (16 * 16)) % 16).id
            if (type == BlockType.OAK_LOG.id)
                logs.add(Vector3i(position.x + i % 16, position.y + (i / 16) % 16, position.z + (i / (16 * 16)) % 16))
            this.blocks[i] = type
        }

        /*for (log in logs) {
            for (x in log.x - 2 .. log.x + 2)
                for (z in log.z - 2 .. log.z + 2)
                    for (y in log.y + 2 .. log.y + 4)
                        World.updateChunk(Vector3i(x, y, z), BlockType.OAK_LEAVES.id)

            for (x in log.x - 1 .. log.x + 1)
                for (z in log.z - 1 .. log.z + 1)
                    for (y in log.y + 4 .. log.y + 5)
                        World.updateChunk(Vector3i(x, y, z), BlockType.OAK_LEAVES.id)

            for (y in log.y .. log.y + 3)
                World.updateChunk(Vector3i(log.x, y, log.z), BlockType.OAK_LOG.id)
        }*/
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

    private fun easeInOutQuint(x: Float): Float {
        return if (x < 0.5) { 16f * x * x * x * x * x } else { 1f - (-2f * x + 2f).toDouble().pow(5.0).toFloat() / 2f }
    }

    private fun getType(x: Int, y: Int, z: Int): BlockType {
        val base = FastNoiseLite()
        base.SetSeed(World.seed)

        base.SetNoiseType(FastNoiseLite.NoiseType.Perlin)

        base.SetFrequency(0.01f)

        base.SetFractalLacunarity(2f)
        base.SetFractalGain(0.5f)
        base.SetFractalOctaves(5)
        base.SetFractalType(FastNoiseLite.FractalType.FBm)

        val mountain = FastNoiseLite()
        mountain.SetSeed(World.seed)

        mountain.SetNoiseType(FastNoiseLite.NoiseType.Perlin)

        mountain.SetFrequency(0.01f)

        mountain.SetFractalLacunarity(2f)
        mountain.SetFractalGain(0.5f)
        mountain.SetFractalOctaves(2)
        mountain.SetFractalType(FastNoiseLite.FractalType.FBm)

        val spike = FastNoiseLite()
        spike.SetSeed(World.seed)

        spike.SetNoiseType(FastNoiseLite.NoiseType.Perlin)

        spike.SetFrequency(0.01f)

        spike.SetFractalLacunarity(4f)
        spike.SetFractalGain(0.5f)
        spike.SetFractalOctaves(3)
        spike.SetFractalType(FastNoiseLite.FractalType.FBm)

        val elevation = FastNoiseLite()
        elevation.SetSeed(World.seed)

        elevation.SetNoiseType(FastNoiseLite.NoiseType.Perlin)

        elevation.SetFrequency(0.01f)

        elevation.SetFractalLacunarity(2f)
        elevation.SetFractalGain(0.5f)
        elevation.SetFractalOctaves(1)
        elevation.SetFractalType(FastNoiseLite.FractalType.FBm)


        val height = round(
            (base.GetNoise(x*0.25f, z*0.25f) * 50) +
                abs(easeInOutQuint(mountain.GetNoise(x*0.2f, z*0.2f))) * 70 +
                abs(easeInOutQuint(spike.GetNoise(x*0.4f, z*0.4f))) * 50 +
                elevation.GetNoise(x*0.005f, z*0.005f) * 500
        ).toInt()

        return if ((y == height || y == height -1 || y == height -2 || y == height -3) && height > 30) BlockType.STONE
        else if (y == height && height < 2) BlockType.SAND
        else if (y == height) BlockType.GRASS
        else if (y < height - 3) BlockType.STONE
        else if (y in (height + 1)..-1) BlockType.WATER
        else if (y > height) BlockType.AIR
        else BlockType.DIRT
    }

}