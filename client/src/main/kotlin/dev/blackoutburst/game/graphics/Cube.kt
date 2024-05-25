package dev.blackoutburst.game.graphics

import dev.blackoutburst.game.Main
import dev.blackoutburst.game.maths.Matrix
import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.game.maths.Vector3f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.*
import org.lwjgl.opengl.ARBInstancedArrays.glVertexAttribDivisorARB
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.glBindVertexArray
import java.nio.Buffer
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

        val vbo = GL15C.glGenBuffers()

        GL30C.glBindVertexArray(vaoID)

        val verticesBuffer = BufferUtils.createFloatBuffer(vertices.size)
        (verticesBuffer.put(vertices) as Buffer).flip()

        GL15C.glBindBuffer(GL15C.GL_ARRAY_BUFFER, vbo)
        GL15C.glBufferData(GL15C.GL_ARRAY_BUFFER, verticesBuffer, GL15C.GL_STATIC_DRAW)

        // Pos
        GL20C.glVertexAttribPointer(0, 3, GL11C.GL_FLOAT, false, 32, 0)
        GL20C.glEnableVertexAttribArray(0)

        // UV
        GL20C.glVertexAttribPointer(1, 2, GL11C.GL_FLOAT, false, 32, 12)
        GL20C.glEnableVertexAttribArray(1)

        // Norm
        GL20C.glVertexAttribPointer(2, 3, GL11C.GL_FLOAT, false, 32, 20)
        GL20C.glEnableVertexAttribArray(2)

        GL15C.glBindBuffer(GL15C.GL_ARRAY_BUFFER, 0)
    }

    private fun setUniforms() {
        val xRad = -rotation.x.toDouble().toFloat()
        val yRad = -rotation.y.toDouble().toFloat()

        model.setIdentity()
            .translate(position)
            .rotate(yRad, Vector3f(cos(xRad), 0f, -sin(xRad)))
            .rotate(xRad, Vector3f(0f, 1f, 0f))

        program.setUniform4f("color", color)
        program.setUniform1f("blockType", Main.blockType.id.toFloat())
        program.setUniform3f("lightColor", Color.WHITE)
        program.setUniform3f("viewPos", Main.camera.position)

        program.setUniformMat4("projection", Main.projection)
        program.setUniformMat4("model", model)
        program.setUniformMat4("view", Main.camera.view)
    }

    fun draw() {
        setUniforms()

        glBindTexture(GL_TEXTURE_2D, 1)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glUseProgram(program.id)
        glBindVertexArray(vaoID)

        glDrawArrays(GL_TRIANGLES, 0, 36)

        glBindVertexArray(0)
        glUseProgram(0)
        glBindTexture(GL_TEXTURE_2D, 0)
    }
}