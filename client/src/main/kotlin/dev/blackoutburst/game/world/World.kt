package dev.blackoutburst.game.world

import dev.blackoutburst.game.graphics.WorldBlock
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.game.maths.Vector3i
import dev.blackoutburst.game.utils.RayCastResult
import dev.blackoutburst.game.utils.chunkFloor
import kotlin.math.round
import kotlin.math.sign

class World {

    companion object {
        val CHUNK_SIZE = 16
    }

    val chunks = mutableMapOf<String, Chunk>()

    var worldBlocks = mutableListOf<WorldBlock>()

    fun getCloseChunk(position: Vector3f): List<Chunk> {
        val indexes = mutableListOf<String>()
        indexes.add(Vector3i(chunkFloor(position.x), chunkFloor(position.y), chunkFloor(position.z)).toString())

        for (x in -1..1) {
            for (y in -1..1) {
                for (z in -1..1) {
                    if (!(x == 0 && y == 0 && z == 0)) {
                            indexes.add(
                                (Vector3i(
                                    chunkFloor(position.x) + x * CHUNK_SIZE,
                                    chunkFloor(position.y) + y * CHUNK_SIZE,
                                    chunkFloor(position.z) + z * CHUNK_SIZE
                                ) / CHUNK_SIZE * CHUNK_SIZE).toString()
                            )
                        }
                }
            }
        }

        return indexes.mapNotNull { chunks[it] }
    }

    fun updateChunk(position: Vector3i, blockData: List<Byte>) {
        val blocks = Array(16) { Array(16) { Array(16) {WorldBlock(BlockType.AIR, Vector3i()) } } }
        var index = 0

        for (x in 0 until CHUNK_SIZE) {
            for (y in 0 until CHUNK_SIZE) {
                for (z in 0 until CHUNK_SIZE) {
                    val data = BlockType.getByID(blockData[index])
                    blocks[x][y][z] = WorldBlock(data, Vector3i(position.x + x, position.y + y, position.z + z))
                    index++
                }
            }
        }

        val blockAsList = blocks.flatMap { it.flatMap { it.toList() } }.filter { it.type != BlockType.AIR }.filter { isFree(position, it, blocks) }
        chunks[position.toString()] = Chunk(position, blocks, blockAsList)

        worldBlocks = chunks.map { it.value.blockAsList }.flatten().toMutableList()

        update()
    }

    private fun isFree(chunkPos: Vector3i, block: WorldBlock, blocks: Array<Array<Array<WorldBlock>>>): Boolean {
        val pos = block.position - chunkPos

        if (pos.x == 0 || pos.y == 0 || pos.z == 0 || pos.x == 15 || pos.y == 15 || pos.z == 15) return true

        if (pos.x > 0 && blocks[pos.x - 1][pos.y][pos.z].type == BlockType.AIR) return true
        if (pos.x < 15 && blocks[pos.x + 1][pos.y][pos.z].type == BlockType.AIR) return true

        if (pos.y > 0 && blocks[pos.x][pos.y - 1][pos.z].type == BlockType.AIR) return true
        if (pos.y < 15 && blocks[pos.x][pos.y + 1][pos.z].type == BlockType.AIR) return true

        if (pos.z > 0 && blocks[pos.x][pos.y][pos.z - 1].type == BlockType.AIR) return true
        if (pos.z < 15 && blocks[pos.x][pos.y][pos.z + 1].type == BlockType.AIR) return true

        return false
    }

    fun update() = WorldBlock.setOffset(worldBlocks)

    fun render() = WorldBlock.draw(worldBlocks.size)

    fun rayCast(start: Vector3f, direction: Vector3f, distance: Float): RayCastResult {
        var currentPosition = start
        val step = direction.normalize() * 0.005f

        for (i in 0 until (distance / step.length()).toInt()) {
            val hitResult = getBlockAt(currentPosition)
            if (hitResult.block != null) {
                return hitResult
            }
            currentPosition += step
        }
        return RayCastResult(null, null)
    }

    fun getChunkIndex(position: Vector3i, chunkSize: Int): Vector3i {
        return Vector3i(
            (if (position.x < 0) (position.x + 1) / chunkSize - 1 else position.x / chunkSize) * chunkSize,
            (if (position.y < 0) (position.y + 1) / chunkSize - 1 else position.y / chunkSize) * chunkSize,
            (if (position.z < 0) (position.z + 1) / chunkSize - 1 else position.z / chunkSize) * chunkSize
        )
    }

    fun getBlockAt(position: Vector3f): RayCastResult {
        val roundedPosition = Vector3i(
            round(position.x).toInt(),
            round(position.y).toInt(),
            round(position.z).toInt()
        )

        val chunkPosition = getChunkIndex(roundedPosition, CHUNK_SIZE)
        val chunkIndex = chunkPosition.toString()

        val chunk = chunks[chunkIndex] ?: return RayCastResult(null, null)

        val positionAsInt = Vector3i(
            ((roundedPosition.x - chunk.position.x) % CHUNK_SIZE + CHUNK_SIZE) % CHUNK_SIZE,
            ((roundedPosition.y - chunk.position.y) % CHUNK_SIZE + CHUNK_SIZE) % CHUNK_SIZE,
            ((roundedPosition.z - chunk.position.z) % CHUNK_SIZE + CHUNK_SIZE) % CHUNK_SIZE
        )

        val block = chunk.blocks[positionAsInt.x][positionAsInt.y][positionAsInt.z]

        if (block.type != BlockType.AIR) {
            val dx = maxOf(position.x - (block.position.x + 0.5), block.position.x - 0.5 - position.x)
            val dy = maxOf(position.y - (block.position.y + 0.5), block.position.y - 0.5 - position.y)
            val dz = maxOf(position.z - (block.position.z + 0.5), block.position.z - 0.5 - position.z)

            val face = when {
                dx > dy && dx > dz -> Vector3i(sign(position.x - block.position.x).toInt(), 0, 0)
                dy > dz -> Vector3i(0, sign(position.y - block.position.y).toInt(), 0)
                else -> Vector3i(0, 0, sign(position.z - block.position.z).toInt())
            }

            return RayCastResult(block, face)
        }
        return RayCastResult(null, null)
    }
}