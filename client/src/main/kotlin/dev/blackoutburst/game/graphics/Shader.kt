package dev.blackoutburst.game.graphics

import org.lwjgl.opengl.GL20
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class Shader (var id: Int) {
    companion object {
        const val VERTEX: Int = GL20.GL_VERTEX_SHADER
        const val FRAGMENT: Int = GL20.GL_FRAGMENT_SHADER

        fun loadShader(shaderType: Int, filePath: String): Shader {
            val shader = GL20.glCreateShader(shaderType)
            val shaderSource = StringBuilder()

            try {
                val stream = Shader::class.java.getResourceAsStream(filePath) ?: throw IOException()

                val reader = BufferedReader(InputStreamReader(stream))
                var line: String?

                while ((reader.readLine().also { line = it }) != null) {
                    shaderSource.append(line).append("//\n")
                }
                reader.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            GL20.glShaderSource(shader, shaderSource)
            GL20.glCompileShader(shader)

            if (GL20.glGetShaderInfoLog(shader).isNotEmpty())
                System.err.println("Error in: [$filePath] ${GL20.glGetShaderInfoLog(shader)}"
            )

            return (Shader(shader))
        }
    }
}
