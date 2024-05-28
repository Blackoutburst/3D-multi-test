package dev.blackoutburst.game.world

import dev.blackoutburst.game.graphics.Shader
import dev.blackoutburst.game.graphics.ShaderProgram
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.game.maths.Vector3i
import dev.blackoutburst.game.utils.RayCastResult
import dev.blackoutburst.game.utils.chunkFloor
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.sign

class World {

    companion object {
        val CHUNK_SIZE = 16
    }

    val chunks = mutableMapOf<String, Chunk>()

    fun getCloseChunk(position: Vector3f): List<Chunk> {
        val indexes = mutableListOf<String>()

        for (x in -1..1) {
            for (y in -1..1) {
                for (z in -1..1) {
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

        return indexes.mapNotNull { chunks[it] }
    }

    fun updateChunk(position: Vector3i, blockData: Array<Byte>) {
        val chunk = Chunk(position, blockData)
        chunk.update()
        chunks[position.toString()] = chunk
    }


    fun render() = chunks.forEach { it.value.render()  }

    fun dda(rayPos: Vector3f, rayDir: Vector3f, maxRaySteps: Int): RayCastResult {
        val mapPos = Vector3i(floor(rayPos.x).toInt(), floor(rayPos.y).toInt(), floor(rayPos.z).toInt())
        val rayDirLength = rayDir.length()
        val deltaDist = Vector3f(abs(rayDirLength / rayDir.x), abs(rayDirLength / rayDir.y), abs(rayDirLength / rayDir.z))
        val rayStep = Vector3i(sign(rayDir.x).toInt(), sign(rayDir.y).toInt(), sign(rayDir.z).toInt())
        val signRayDir = Vector3f(sign(rayDir.x), sign(rayDir.y), sign(rayDir.z))
        val mapPosVec3 = Vector3f(mapPos.x.toFloat(), mapPos.y.toFloat(), mapPos.z.toFloat())
        val sideDist = (signRayDir * (mapPosVec3 - rayPos) + (signRayDir * 0.5f) + 0.5f) * deltaDist
        var mask = Vector3i()

        for (i in 0 until maxRaySteps) {
            val block = getBlockAt(mapPos)
            if (block != null) return RayCastResult(block, mask)

            if (sideDist.x < sideDist.y) {
                if (sideDist.x < sideDist.z) {
                    sideDist.x += deltaDist.x
                    mapPos.x += rayStep.x
                    mask = Vector3i(-rayStep.x, 0, 0)
                } else {
                    sideDist.z += deltaDist.z
                    mapPos.z += rayStep.z
                    mask = Vector3i(0, 0, -rayStep.z)
                }
            } else {
                if (sideDist.y < sideDist.z) {
                    sideDist.y += deltaDist.y
                    mapPos.y += rayStep.y
                    mask = Vector3i(0, -rayStep.y, 0)
                } else {
                    sideDist.z += deltaDist.z
                    mapPos.z += rayStep.z
                    mask = Vector3i(0, 0, -rayStep.z)
                }
            }
        }

        return RayCastResult(null, mask)
    }

    fun getBlockAt(position: Vector3i): Block? {
        val chunkPosition = Chunk.getIndex(position, CHUNK_SIZE)
        val chunk = chunks[chunkPosition.toString()] ?: return null
        val positionAsInt = Vector3i(
            ((position.x - chunk.position.x) % CHUNK_SIZE + CHUNK_SIZE) % CHUNK_SIZE,
            ((position.y - chunk.position.y) % CHUNK_SIZE + CHUNK_SIZE) % CHUNK_SIZE,
            ((position.z - chunk.position.z) % CHUNK_SIZE + CHUNK_SIZE) % CHUNK_SIZE
        )

        val blockId = chunk.xyzToIndex(positionAsInt.x, positionAsInt.y, positionAsInt.z)
        val blockType = chunk.blocks[blockId]
        if (blockType == BlockType.AIR.id) return null

        return Block(BlockType.getByID(chunk.blocks[blockId]), chunk.indexToXYZ(blockId))
    }
}