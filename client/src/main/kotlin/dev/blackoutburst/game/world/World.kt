package dev.blackoutburst.game.world

import dev.blackoutburst.game.Main
import dev.blackoutburst.game.core.Display
import dev.blackoutburst.game.graphics.*
import dev.blackoutburst.game.main
import dev.blackoutburst.game.maths.Matrix
import dev.blackoutburst.game.maths.Vector2f
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
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.sign

class World {

    private val vertexShader = Shader.loadShader(Shader.VERTEX, "/shaders/phongChunk.vert")
    private val fragmentShader = Shader.loadShader(Shader.FRAGMENT, "/shaders/phongChunk.frag")
    private val chunkProgram = ShaderProgram(vertexShader, fragmentShader)

    private val minimapShader = Shader.loadShader(Shader.FRAGMENT, "/shaders/minimapChunk.frag")
    private val minimapProgram = ShaderProgram(vertexShader, minimapShader)

    private val frameBufferVertexShader = Shader.loadShader(Shader.VERTEX, "/shaders/frameBuffer.vert")
    private val frameBufferFragmentShader = Shader.loadShader(Shader.FRAGMENT, "/shaders/frameBuffer.frag")
    private val frameBufferChunkProgram = ShaderProgram(frameBufferVertexShader, frameBufferFragmentShader)

    val framebufferVAO = createFrameBufferVAO()

    val framebufferData = createFramebuffer(400, 400)

    var diffuseMap = 0

    var chunkUpdate = AtomicInteger(0)

    var blockCount = 0
    var vertexCount = 0
    var chunkCount = 0

    companion object {
        val CHUNK_SIZE = 16

        val vertices = floatArrayOf(
            -1f, -1f, 0f, 0f,
            1f,  1f, 1f, 1f,
            -1f, 1f, 0f, 1f,
            1f, -1f, 1f, 0f,
        )

        val indices = intArrayOf(
            0, 1, 2,
            0, 3, 1,
        )

        fun createFrameBufferVAO(): Int {
            val vaoID = glGenVertexArrays()
            val vboID = glGenBuffers()
            val eboID = glGenBuffers()

            glBindVertexArray(vaoID)

            stack { stack ->
                glBindBuffer(GL_ARRAY_BUFFER, vboID)

                val vertexBuffer = stack.mallocFloat(vertices.size)
                vertexBuffer.put(vertices).flip()

                glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW)

                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID)

                val indexBuffer = stack.mallocInt(indices.size)
                indexBuffer.put(indices).flip()

                glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW)

                glEnableVertexAttribArray(0)
                glVertexAttribPointer(0, 2, GL_FLOAT, false, 16, 0)
                glEnableVertexAttribArray(1)
                glVertexAttribPointer(1, 2, GL_FLOAT, false, 16, 8)
                glBindVertexArray(0)
            }
            return vaoID
        }
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
        chunks[position.toString()]?.let {
            it.blocks = blockData
            it.update()
        } ?: run {
            val chunk = Chunk(position, blockData)
            chunk.update()
            chunks[position.toString()] = chunk
        }
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

    fun createTextureMaps() {
        diffuseMap = TextureArray(Textures.entries.map { it.file }).id
    }

    fun createFramebuffer(width: Int, height: Int): Pair<Int, Int> {
        val fbo = glGenFramebuffers()
        glBindFramebuffer(GL_FRAMEBUFFER, fbo)

        val texture = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, texture)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, 0)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture, 0)

        val rbo = glGenRenderbuffers()
        glBindRenderbuffer(GL_RENDERBUFFER, rbo)
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, width, height)
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, rbo)

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            println("Error: Framebuffer is not complete!")
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        return Pair(fbo, texture)
    }

    fun render() {
        glUseProgram(minimapProgram.id)
        glBindFramebuffer(GL_FRAMEBUFFER, framebufferData.first)
        glViewport(0, 0, 400, 400)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        glBindTexture(GL_TEXTURE_2D_ARRAY, diffuseMap)

        val a = 70f

        minimapProgram.setUniformMat4("projection", Matrix()
            .ortho2D(-a, a, -a, a, -1000f, 1000f))

        minimapProgram.setUniformMat4("view", Matrix()
            .rotate(Math.toRadians(90.0).toFloat(), Vector3f(1f, 0f, 0f))
            .translate(Vector3f(-Main.camera.position.x, -50f, -Main.camera.position.z))
        )

        povRender(true)

        glBindFramebuffer(GL_FRAMEBUFFER, 0)

        glViewport(0, 0, Display.getWidth(), Display.getHeight())

        glUseProgram(chunkProgram.id)
        chunkProgram.setUniform4f("color", Color.WHITE)
        chunkProgram.setUniform3f("lightColor", Color.WHITE)
        chunkProgram.setUniform3f("viewPos", Main.camera.position)

        chunkProgram.setUniformMat4("projection", Main.projection)
        chunkProgram.setUniformMat4("view", Main.camera.view)

        povRender(false)

        glDisable(GL_DEPTH_TEST)
        glDisable(GL_CULL_FACE)

        glUseProgram(frameBufferChunkProgram.id)
        glViewport(0, 0, 400, 400)

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, framebufferData.second)

        frameBufferChunkProgram.setUniform1i("diffuseMap", 0)

        glBindVertexArray(framebufferVAO)

        glDrawElements(GL_TRIANGLES, indices.size, GL_UNSIGNED_INT, 0)

        glBindVertexArray(0)

        glEnable(GL_DEPTH_TEST)
        glEnable(GL_CULL_FACE)
    }

    fun povRender(minimap: Boolean) {
        blockCount = 0
        vertexCount = 0
        chunkCount = 0

        if (!minimap)
            FrustumCulling.updateFrustum(Main.projection, Main.camera.view)

        for (chunk in chunks.values) {
            if (chunk.indexCount == 0) continue
            val minCorner = chunk.position
            val maxCorner = chunk.position + Vector3i(16)

            if (minimap && distance(Vector2f(Main.camera.position.x, Main.camera.position.z), Vector2f(chunk.position.x.toFloat(), chunk.position.z.toFloat())) >= 15000)
                continue

            if (!minimap && !FrustumCulling.isBoxInFrustum(
                    minCorner.x.toFloat(),
                    minCorner.y.toFloat(),
                    minCorner.z.toFloat(),
                    maxCorner.x.toFloat(),
                    maxCorner.y.toFloat(),
                    maxCorner.z.toFloat()
                )) {
                continue
            }

            vertexCount += chunk.indexCount
            blockCount += chunk.blockCount
            chunkCount++
            chunkProgram.setUniform3f("chunkPos", chunk.position.toFloat())
            minimapProgram.setUniform3f("chunkPos", chunk.position.toFloat())
            chunk.render()
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