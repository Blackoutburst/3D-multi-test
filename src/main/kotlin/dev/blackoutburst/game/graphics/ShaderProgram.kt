package dev.blackoutburst.game.graphics

import dev.blackoutburst.game.maths.Matrix
import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.game.maths.Vector4f
import org.lwjgl.opengl.ARBProgramInterfaceQuery
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL41

class ShaderProgram(
    private var vertex: Shader,
    private var fragment: Shader
) {
    var id: Int = 0

    init {
        this.createProgram()
    }

    private fun createProgram() {
        id = GL20.glCreateProgram()
        GL20.glAttachShader(id, vertex.id)
        GL20.glAttachShader(id, fragment.id)
        GL20.glLinkProgram(id)
        GL20.glDetachShader(id, vertex.id)
        GL20.glDetachShader(id, fragment.id)
    }

    fun clean() {
        GL20.glDeleteProgram(id)
    }

    fun setUniform1f(varName: String, x: Float) {
        val loc: Int = ARBProgramInterfaceQuery.glGetProgramResourceLocation(id, ARBProgramInterfaceQuery.GL_UNIFORM, varName)
        GL41.glProgramUniform1f(id, loc, x)
    }

    fun setUniform2f(varName: String, x: Float, y: Float) {
        val loc: Int =
            ARBProgramInterfaceQuery.glGetProgramResourceLocation(id, ARBProgramInterfaceQuery.GL_UNIFORM, varName)
        GL41.glProgramUniform2f(id, loc, x, y)
    }

    fun setUniform2f(varName: String, vec: Vector2f) {
        val loc: Int =
            ARBProgramInterfaceQuery.glGetProgramResourceLocation(id, ARBProgramInterfaceQuery.GL_UNIFORM, varName)
        GL41.glProgramUniform2f(id, loc, vec.x, vec.y)
    }

    fun setUniform3f(varName: String, x: Float, y: Float, z: Float) {
        val loc: Int =
            ARBProgramInterfaceQuery.glGetProgramResourceLocation(id, ARBProgramInterfaceQuery.GL_UNIFORM, varName)
        GL41.glProgramUniform3f(id, loc, x, y, z)
    }

    fun setUniform3f(varName: String, vec: Vector3f) {
        val loc: Int =
            ARBProgramInterfaceQuery.glGetProgramResourceLocation(id, ARBProgramInterfaceQuery.GL_UNIFORM, varName)
        GL41.glProgramUniform3f(id, loc, vec.x, vec.y, vec.z)
    }

    fun setUniform3f(varName: String, color: Color) {
        val loc: Int =
            ARBProgramInterfaceQuery.glGetProgramResourceLocation(id, ARBProgramInterfaceQuery.GL_UNIFORM, varName)
        GL41.glProgramUniform3f(id, loc, color.r, color.g, color.b)
    }

    fun setUniform4f(varName: String, x: Float, y: Float, z: Float, w: Float) {
        val loc: Int =
            ARBProgramInterfaceQuery.glGetProgramResourceLocation(id, ARBProgramInterfaceQuery.GL_UNIFORM, varName)
        GL41.glProgramUniform4f(id, loc, x, y, z, w)
    }

    fun setUniform4f(varName: String, vec: Vector4f) {
        val loc: Int =
            ARBProgramInterfaceQuery.glGetProgramResourceLocation(id, ARBProgramInterfaceQuery.GL_UNIFORM, varName)
        GL41.glProgramUniform4f(id, loc, vec.x, vec.y, vec.z, vec.w)
    }

    fun setUniform4f(varName: String, color: Color) {
        val loc: Int =
            ARBProgramInterfaceQuery.glGetProgramResourceLocation(id, ARBProgramInterfaceQuery.GL_UNIFORM, varName)
        GL41.glProgramUniform4f(id, loc, color.r, color.g, color.b, color.a)
    }

    fun setUniformMat4(varName: String, mat: Matrix) {
        val loc: Int =
            ARBProgramInterfaceQuery.glGetProgramResourceLocation(id, ARBProgramInterfaceQuery.GL_UNIFORM, varName)
        GL41.glProgramUniformMatrix4fv(id, loc, false, mat.getValues())
    }
}
