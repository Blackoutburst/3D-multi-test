package dev.blackoutburst.game.world

import dev.blackoutburst.game.graphics.WorldBlock
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.game.utils.OpenSimplex2
import dev.blackoutburst.game.utils.RayCastResult
import kotlin.math.sign

class World {

    val CHUNK_SIZE = 16

    val chunks = mutableListOf<Chunk>()

    fun placeChunk(position: Vector3f, blockData: List<Byte>) {
        val blocks = mutableListOf<WorldBlock>()
        var index = 0

        for (x in 0 until CHUNK_SIZE) {
            for (y in 0 until CHUNK_SIZE) {
                for (z in 0 until CHUNK_SIZE) {
                    blocks.add(WorldBlock(
                        type = blockData[index],
                        position = Vector3f(
                            position.x + x,
                            position.y + y,
                            position.z + z
                        )
                    ))
                    index++
                }
            }
        }

        chunks.add(Chunk(
            position,
            blocks
        ))
        update()
    }

    private fun update() {
        val blocks = chunks.map {
            it.blocks
        }.flatten()

        WorldBlock.setOffset(blocks)
    }

    fun render() {
        val blocks = chunks.map {
            it.blocks
        }.flatten()


        WorldBlock.draw(blocks.size)
    }

    /*
    fun destroyBlock(block: WorldBlock) {
        blocks.remove(block)
        update()
    }

    fun placeBlock(block: WorldBlock) {
        if (getBlockAt(block.position).block != null) return

        blocks.add(block)
        update()
    }
     */

    /*
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
        blocks.find { block ->
            block.position.x - 0.5 <= position.x && block.position.x + 0.5 >= position.x &&
                    block.position.y - 0.5 <= position.y && block.position.y + 0.5 >= position.y &&
                    block.position.z - 0.5 <= position.z && block.position.z + 0.5 >= position.z
        }?.let { block ->
            val dx = maxOf(position.x - (block.position.x + 0.5), block.position.x - 0.5 - position.x)
            val dy = maxOf(position.y - (block.position.y + 0.5), block.position.y - 0.5 - position.y)
            val dz = maxOf(position.z - (block.position.z + 0.5), block.position.z - 0.5 - position.z)

            val face = when {
                dx > dy && dx > dz -> Vector3f(sign(position.x - block.position.x), 0f, 0f)
                dy > dz -> Vector3f(0f, sign(position.y - block.position.y), 0f)
                else -> Vector3f(0f, 0f, sign(position.z - block.position.z))
            }

            return RayCastResult(block, face)
        }
        return RayCastResult(null, null)
    }
    */

}