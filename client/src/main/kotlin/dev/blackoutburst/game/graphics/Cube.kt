package dev.blackoutburst.game.graphics

import dev.blackoutburst.game.Main
import dev.blackoutburst.game.maths.Matrix
import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.game.maths.Vector3f
import org.lwjgl.opengl.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.system.MemoryUtil
import kotlin.math.cos
import kotlin.math.sin


class Cube(var position: Vector3f, var rotation: Vector2f, var color: Color) {
    private val vaoID = GL30C.glGenVertexArrays()
    private val vertices = floatArrayOf(
        //FRONT
        -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f,
        0.5f, 0.5f, -0.5f, 1.0f, 1.0f, 0.0f, 0.0f, -1.0f,
        0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f,
        0.5f, 0.5f, -0.5f, 1.0f, 1.0f, 0.0f, 0.0f, -1.0f,
        -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f,
        -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f,  //BACK

        -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
        0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
        0.5f, 0.5f, 0.5f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
        0.5f, 0.5f, 0.5f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
        -0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
        -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,  //LEFT

        -0.5f, 0.5f, 0.5f, 1.0f, 0.0f, -1.0f, 0.0f, 0.0f,
        -0.5f, 0.5f, -0.5f, 1.0f, 1.0f, -1.0f, 0.0f, 0.0f,
        -0.5f, -0.5f, -0.5f, 0.0f, 1.0f, -1.0f, 0.0f, 0.0f,
        -0.5f, -0.5f, -0.5f, 0.0f, 1.0f, -1.0f, 0.0f, 0.0f,
        -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
        -0.5f, 0.5f, 0.5f, 1.0f, 0.0f, -1.0f, 0.0f, 0.0f,  //RIGHT

        0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f,
        0.5f, -0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f,
        0.5f, 0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f,
        0.5f, -0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f,
        0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f,
        0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,  //BOTTOM

        -0.5f, -0.5f, -0.5f, 0.0f, 1.0f, 0.0f, -1.0f, 0.0f,
        0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 0.0f, -1.0f, 0.0f,
        0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f,
        0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f,
        -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f,
        -0.5f, -0.5f, -0.5f, 0.0f, 1.0f, 0.0f, -1.0f, 0.0f,  //TOP

        -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
        0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
        0.5f, 0.5f, -0.5f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f,
        0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
        -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
        -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
    )
    private val vertexShader = Shader.loadShader(Shader.VERTEX, "/shaders/bounding.vert")
    private val fragmentShader = Shader.loadShader(Shader.FRAGMENT, "/shaders/bounding.frag")
    private val program = ShaderProgram(vertexShader, fragmentShader)
    private val model = Matrix()

    init {
        val vboId = glGenBuffers()

        glBindVertexArray(vaoID)
        glBindBuffer(GL_ARRAY_BUFFER, vboId)


        val vertexBuffer = MemoryUtil.memAllocFloat(vertices.size)
        vertexBuffer.put(vertices).flip()

        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW)
        MemoryUtil.memFree(vertexBuffer)

        // Pos
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 32, 0)
        glEnableVertexAttribArray(0)

        // UV
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 32, 12)
        glEnableVertexAttribArray(1)

        // Norm
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 32, 20)
        glEnableVertexAttribArray(2)

        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

    private fun setUniforms() {
        val xRad = -rotation.x.toDouble().toFloat()
        val yRad = -rotation.y.toDouble().toFloat()

        model.setIdentity()
            .translate(position)
            .rotate(yRad, Vector3f(cos(xRad), 0f, -sin(xRad)))
            .rotate(xRad, Vector3f(0f, 1f, 0f))

        program.setUniform4f("color", color)
        program.setUniform1f("blockType", Main.blockType.textures[0].toFloat())
        program.setUniform3f("lightColor", Color.WHITE)
        program.setUniform3f("viewPos", Main.camera.position)

        program.setUniformMat4("projection", Main.projection)
        program.setUniformMat4("model", model)
        program.setUniformMat4("view", Main.camera.view)
    }

    fun draw() {
        setUniforms()

        glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, 1)

        glUseProgram(program.id)
        glBindVertexArray(vaoID)

        glDrawArrays(GL_TRIANGLES, 0, 36)

        glBindVertexArray(0)
        glUseProgram(0)
        glBindTexture(GL_TEXTURE_2D, 0)
    }
}