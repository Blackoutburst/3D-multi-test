package dev.blackoutburst.game.graphics

import Main
import dev.blackoutburst.game.core.Shader
import dev.blackoutburst.game.core.ShaderProgram
import dev.blackoutburst.game.maths.Matrix
import dev.blackoutburst.game.maths.Vector3f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.*
import org.lwjgl.opengl.ARBInstancedArrays.glVertexAttribDivisorARB
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL31.glDrawArraysInstanced
import java.nio.Buffer


class Block(val position: Vector3f) {
    companion object {
        private var vaoID = 0

        private val VERTICES = floatArrayOf(
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

        private val vertexShader = Shader.loadShader(Shader.VERTEX, "/shaders/cube.vert")
        private val fragmentShader = Shader.loadShader(Shader.FRAGMENT, "/shaders/cube.frag")
        private val program = ShaderProgram(vertexShader, fragmentShader)

        init {
            vaoID = GL30C.glGenVertexArrays()

            val vbo = GL15C.glGenBuffers()

            GL30C.glBindVertexArray(vaoID)

            val verticesBuffer = BufferUtils.createFloatBuffer(VERTICES.size)
            (verticesBuffer.put(VERTICES) as Buffer).flip()

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
            program.setUniform3f("color", Color(0.2f, 0.8f, 0.1f))
            program.setUniform3f("lightColor", Color.WHITE)
            program.setUniform3f("viewPos", Main.camera.position)

            program.setUniformMat4("projection", Main.projection)
            program.setUniformMat4("model", Matrix())
            program.setUniformMat4("view", Main.camera.view)
        }

        fun setOffset(blocks: List<Block>) {
            val size = blocks.size
            val translation = FloatArray(size * 3)

            var idx = 0
            for (i in 0 until size) {
                translation[idx] = blocks[i].position.x
                translation[idx + 1] = blocks[i].position.y
                translation[idx + 2] = blocks[i].position.z
                idx += 3
            }

            val instanceVBO: Int = glGenBuffers()
            val offsetBuffer = BufferUtils.createFloatBuffer(translation.size)
            (offsetBuffer.put(translation) as Buffer).flip()

            glBindVertexArray(vaoID)
            glBindBuffer(GL_ARRAY_BUFFER, instanceVBO)
            glBufferData(GL_ARRAY_BUFFER, offsetBuffer, GL_STATIC_DRAW)
            glBindBuffer(GL_ARRAY_BUFFER, 0)

            glEnableVertexAttribArray(3)
            glBindBuffer(GL_ARRAY_BUFFER, instanceVBO)
            glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, 0)
            glBindBuffer(GL_ARRAY_BUFFER, 0)
            glVertexAttribDivisorARB(3, 1)
            glBindVertexArray(0)

            glDeleteBuffers(instanceVBO)

            (offsetBuffer as Buffer).clear()
        }

        fun draw(size: Int) {
            setUniforms()

            glBindTexture(GL_TEXTURE_2D, 1)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)

            glUseProgram(program.id)
            glBindVertexArray(vaoID)

            glDrawArraysInstanced(GL_TRIANGLES, 0, 36, size)
        }
    }
}