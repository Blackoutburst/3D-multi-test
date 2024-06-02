package dev.blackoutburst.game.world

import dev.blackoutburst.game.Main
import dev.blackoutburst.game.core.Display
import dev.blackoutburst.game.graphics.*
import dev.blackoutburst.game.main
import dev.blackoutburst.game.maths.Matrix
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.game.maths.Vector3i
import dev.blackoutburst.game.utils.*
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL42.glDrawArraysInstancedBaseInstance
import org.lwjgl.system.MemoryUtil.NULL
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.sign

class World {

    private val vertexShader = Shader.loadShader(Shader.VERTEX, "/shaders/phongChunk.vert")
    private val fragmentShader = Shader.loadShader(Shader.FRAGMENT, "/shaders/phongChunk.frag")
    private val chunkProgram = ShaderProgram(vertexShader, fragmentShader)

    var diffuseMap = 0

    companion object {
        val CHUNK_SIZE = 16
    }

    val chunks: ConcurrentHashMap<String, Chunk> = ConcurrentHashMap()

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

    fun removeChunk(chunk: Chunk) {
        main {
            glDeleteVertexArrays(chunk.vboID)
            glDeleteBuffers(chunk.vboID)
            glDeleteBuffers(chunk.eboID)
        }

        chunks.remove(chunk.position.toString())
    }

    fun addChunk(position: Vector3i, blockData: Array<Byte>) {
        val chunk = Chunk(position, blockData)
        chunk.update()

        chunks[position.toString()] = chunk
        updateAdjacentChunk(position)
    }

    fun updateAdjacentChunk(position: Vector3i) {
        chunks[Vector3i(position.x + CHUNK_SIZE, position.y, position.z).toString()]?.update()
        chunks[Vector3i(position.x - CHUNK_SIZE, position.y, position.z).toString()]?.update()
        chunks[Vector3i(position.x, position.y + CHUNK_SIZE, position.z).toString()]?.update()
        chunks[Vector3i(position.x, position.y - CHUNK_SIZE, position.z).toString()]?.update()
        chunks[Vector3i(position.x, position.y, position.z + CHUNK_SIZE).toString()]?.update()
        chunks[Vector3i(position.x, position.y, position.z - CHUNK_SIZE).toString()]?.update()
    }

    private fun setUniforms() {
        chunkProgram.setUniform4f("color", Color.WHITE)
        chunkProgram.setUniform3f("lightColor", Color.WHITE)
        chunkProgram.setUniform3f("viewPos", Main.camera.position)

        chunkProgram.setUniformMat4("projection", Main.projection)
        chunkProgram.setUniformMat4("view", Main.camera.view)
    }

    fun createTextureMaps() {
        diffuseMap = TextureArray(Textures.entries.map { it.file }).id
    }

    fun render() {
        setUniforms()

        glBindTexture(GL_TEXTURE_2D_ARRAY, diffuseMap)

        glUseProgram(chunkProgram.id)
        chunks.forEach {
            chunkProgram.setUniform3f("chunkPos", it.value.position.toFloat())
            it.value.render()
        }
    }

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

        return Block(BlockType.getByID(chunk.blocks[blockId]), chunk.indexToXYZPosition(blockId))
    }
}