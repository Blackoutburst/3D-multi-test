package dev.blackoutburst.game.world

import dev.blackoutburst.game.Main
import dev.blackoutburst.game.core.Display
import dev.blackoutburst.game.graphics.*
import dev.blackoutburst.game.maths.Matrix
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.game.maths.Vector3i
import dev.blackoutburst.game.utils.RayCastResult
import dev.blackoutburst.game.utils.Textures
import dev.blackoutburst.game.utils.chunkFloor
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil.NULL
import java.text.NumberFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.sign

class World {

    private val vertexShader = Shader.loadShader(Shader.VERTEX, "/shaders/chunk.vert")
    private val fragmentShader = Shader.loadShader(Shader.FRAGMENT, "/shaders/chunk.frag")
    private val chunkProgram = ShaderProgram(vertexShader, fragmentShader)

    private val depthVertexShader = Shader.loadShader(Shader.VERTEX, "/shaders/shadow.vert")
    private val depthFragmentShader = Shader.loadShader(Shader.FRAGMENT, "/shaders/shadow.frag")
    private val depthProgram = ShaderProgram(depthVertexShader, depthFragmentShader)

    var depthMap = 0
    val depthMapSZize = 4096
    var depthMapFBO = 0
    var normalMap = 0

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

        chunks[Vector3i(position.x + CHUNK_SIZE, position.y, position.z).toString()]?.update()
        chunks[Vector3i(position.x - CHUNK_SIZE, position.y, position.z).toString()]?.update()
        chunks[Vector3i(position.x, position.y + CHUNK_SIZE, position.z).toString()]?.update()
        chunks[Vector3i(position.x, position.y - CHUNK_SIZE, position.z).toString()]?.update()
        chunks[Vector3i(position.x, position.y, position.z + CHUNK_SIZE).toString()]?.update()
        chunks[Vector3i(position.x, position.y, position.z - CHUNK_SIZE).toString()]?.update()
    }


    private fun setUniforms() {
        val lightPos = Vector3f(-30.0f, 50.0f, -20.0f)
        val projection = Matrix().ortho2D(-100.0f, 100.0f, -100.0f, 100.0f, 30.0f, 100f)
        val view = Matrix().lookAt(
            lightPos,
            Vector3f(0.0f, 0.0f, 0.0f),
            Vector3f(0.0f, 1.0f, 0.0f))

        depthProgram.setUniformMat4("projection", projection)
        depthProgram.setUniformMat4("view", view)

        chunkProgram.setUniform1i("shadowMap", 1)
        chunkProgram.setUniform1i("normalMap", 2)
        chunkProgram.setUniform3f("lightPos", lightPos)
        chunkProgram.setUniformMat4("lightProjection", projection)
        chunkProgram.setUniformMat4("lightView", view)

        chunkProgram.setUniform4f("color", Color.WHITE)
        chunkProgram.setUniform3f("lightColor", Color.WHITE)
        chunkProgram.setUniform3f("viewPos", Main.camera.position)

        chunkProgram.setUniformMat4("projection", Main.projection)
        chunkProgram.setUniformMat4("view", Main.camera.view)
    }

    fun createDepthMap() {
        normalMap = TextureArray(Textures.entries.map { "normal/${it.file}" }, 256).id
        depthMap = glGenTextures()

        depthMapFBO = glGenFramebuffers()

        glBindTexture(GL_TEXTURE_2D, depthMap)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER)
        val borderColor = floatArrayOf(1.0.toFloat(), 1.0.toFloat(), 1.0.toFloat(), 1.0.toFloat())
        glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, borderColor)

        glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, depthMapSZize, depthMapSZize, 0, GL_DEPTH_COMPONENT, GL_FLOAT, NULL)
        glDrawBuffer(GL_NONE)
        glReadBuffer(GL_NONE)
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthMap, 0)
    }

    fun render() {
        setUniforms()

        glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO)

        glUseProgram(depthProgram.id)

        glViewport(0, 0, depthMapSZize, depthMapSZize)
        glClear(GL_DEPTH_BUFFER_BIT)

        chunks.forEach { it.value.render() }
        glBindFramebuffer(GL_FRAMEBUFFER, 0)

        glViewport(0, 0, Display.getWidth(), Display.getHeight())
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)


        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D_ARRAY, 1)
        glActiveTexture(GL_TEXTURE1)
        glBindTexture(GL_TEXTURE_2D, depthMap)
        glActiveTexture(GL_TEXTURE2)
        glBindTexture(GL_TEXTURE_2D_ARRAY, normalMap)

        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST)
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
        glUseProgram(chunkProgram.id)
        chunks.forEach { it.value.render() }
    }

    private fun printVertexCount() {
        val vCount = chunks.map { it.value.vertexCount }.sum()
        val format = NumberFormat.getNumberInstance(Locale.FRANCE)

        println("Total vertex count: ${format.format(vCount).replace(',', ' ')}")
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

        return Block(BlockType.getByID(chunk.blocks[blockId]), chunk.indexToXYZ(blockId))
    }
}