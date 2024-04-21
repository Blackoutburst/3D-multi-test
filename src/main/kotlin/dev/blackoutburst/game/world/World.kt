package dev.blackoutburst.game.world

import dev.blackoutburst.game.graphics.Block
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.game.utils.OpenSimplex2
import dev.blackoutburst.game.utils.RayCastResult
import kotlin.math.sign

class World(private val size: Int = 20) {

    val blocks = mutableListOf<Block>()

    fun generate() {
        for (x in -size/2 until size/2) {
            for (z in -size/2 until size/2) {
                blocks.add(Block(
                    position = Vector3f(x.toFloat(), (OpenSimplex2.noise2(8, x / 100.0, z / 100.0) * 7).toInt().toFloat(), z.toFloat()),
                ))
            }
        }

        update()
    }

    private fun update() {
        Block.setOffset(blocks)
    }

    fun render() {
        Block.draw(blocks.size)
    }

    fun destroyBlock(block: Block) {
        blocks.remove(block)
        update()
    }

    fun placeBlock(block: Block) {
        if (getBlockAt(block.position).block != null) return

        blocks.add(block)
        update()
    }

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
            // Determine which face is hit
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

}