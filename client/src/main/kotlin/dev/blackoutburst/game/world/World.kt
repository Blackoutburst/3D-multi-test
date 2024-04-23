package dev.blackoutburst.game.world

import dev.blackoutburst.game.graphics.WorldBlock
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.game.maths.Vector3i
import dev.blackoutburst.game.utils.OpenSimplex2
import dev.blackoutburst.game.utils.RayCastResult
import dev.blackoutburst.game.utils.chunkFloor
import kotlin.math.floor
import kotlin.math.sign

class World {

    companion object {
        val CHUNK_SIZE = 16
    }

    val chunks = mutableMapOf<String, Chunk>()

    var worldBlocks = listOf<WorldBlock>()

    fun getCloseBlocks(position: Vector3f): List<WorldBlock> {
        val indexes = mutableListOf<String>()

        for (x in -1..1) {
            for (y in -1..1) {
                for (z in -1..1) {
                        indexes.add(
                            (Vector3i(chunkFloor(position.x) + x * CHUNK_SIZE, chunkFloor(position.y) + y * CHUNK_SIZE, chunkFloor(position.z) + z * CHUNK_SIZE) / CHUNK_SIZE * CHUNK_SIZE).toString()
                    )
                }
            }
        }

        val blocks = indexes.map {
            chunks[it]?.getSolidBlock() ?: emptyList()
        }.flatten()

        return blocks
    }

    fun updateChunk(position: Vector3i, blockData: List<Byte>) {
        val blocks = mutableListOf<WorldBlock>()
        var index = 0

        for (x in 0 until CHUNK_SIZE) {
            for (y in 0 until CHUNK_SIZE) {
                for (z in 0 until CHUNK_SIZE) {
                    blocks.add(WorldBlock(
                        type = BlockType.getByID(blockData[index]),
                        position = Vector3i(
                            position.x + x,
                            position.y + y,
                            position.z + z
                        )
                    ))
                    index++
                }
            }
        }

        chunks[position.toString()] = Chunk(position, blocks)

        worldBlocks = chunks.map {
            it.value.getSolidBlock()
        }.flatten().toList()

        update()
    }

    private fun update() = WorldBlock.setOffset(worldBlocks)

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

    fun getBlockAt(position: Vector3f): RayCastResult {
        val index = (Vector3i(
            chunkFloor(position.x),
            chunkFloor(position.y),
            chunkFloor(position.z)
        ) / CHUNK_SIZE * CHUNK_SIZE).toString()
        val blocks = chunks[index]?.getSolidBlock() ?: emptyList()

        blocks.find { block ->
            block.position.x - 0.5 <= position.x && block.position.x + 0.5 >= position.x &&
                    block.position.y - 0.5 <= position.y && block.position.y + 0.5 >= position.y &&
                    block.position.z - 0.5 <= position.z && block.position.z + 0.5 >= position.z
        }?.let { block ->
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