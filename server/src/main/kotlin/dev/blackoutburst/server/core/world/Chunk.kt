package dev.blackoutburst.server.core.world

import dev.blackoutburst.server.maths.Vector3i
import dev.blackoutburst.server.utils.FastNoiseLite
import kotlin.math.round
import kotlin.random.Random


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

    private fun getType(x: Int, y: Int, z: Int): BlockType {
        val noise = FastNoiseLite()
        noise.SetSeed(World.seed)

        noise.SetNoiseType(FastNoiseLite.NoiseType.Perlin)

        noise.SetFrequency(0.01f)

        noise.SetFractalLacunarity(2f)
        noise.SetFractalGain(0.5f)
        noise.SetFractalOctaves(6)
        noise.SetFractalType(FastNoiseLite.FractalType.FBm)

        noise.SetDomainWarpType(FastNoiseLite.DomainWarpType.OpenSimplex2)
        noise.SetDomainWarpAmp(50000f)

        val height = round(noise.GetNoise(x*0.25f, z*0.25f) * 100).toInt() + 10

        return if (y == height && height < 2) BlockType.SAND
        //else if (y == height + 1 && Random.nextInt(500) == 0 && height >= 2) BlockType.OAK_LOG
        else if (y == height) BlockType.GRASS
        else if (y < height - 3) BlockType.STONE
        else if (y > height && y < 0) BlockType.WATER
        else if (y > height) BlockType.AIR
        else BlockType.DIRT
    }

}