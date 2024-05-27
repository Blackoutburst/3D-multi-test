package dev.blackoutburst.game.graphics

import dev.blackoutburst.game.utils.IOUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import org.lwjgl.opengl.GL30
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer

class TextureArray(filePaths: List<String>) {
    private val width: Int = 16
    private val height: Int = 16
    var id: Int = 0

    init {
        MemoryStack.stackPush().use { stack ->
            val widthBuffer = stack.mallocInt(1)
            val heightBuffer = stack.mallocInt(1)
            val comp = stack.mallocInt(1)

            id = GL11.glGenTextures()
            GL11.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, id)
            GL30.glTexImage3D(
                GL30.GL_TEXTURE_2D_ARRAY, 0, GL11.GL_RGBA8,
                width, height, filePaths.size, 0,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, null as ByteBuffer?
            )

            for ((index, filePath) in filePaths.withIndex()) {
                val data = STBImage.stbi_load_from_memory(IOUtils.ioResourceToByteBuffer(filePath, 1024), widthBuffer, heightBuffer, comp, 4)
                    ?: continue

                GL30.glTexSubImage3D(
                    GL30.GL_TEXTURE_2D_ARRAY, 0, 0, 0, index,
                    width, height, 1, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data
                )
                STBImage.stbi_image_free(data)
            }

            GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
            GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
            GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE)
            GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE)
        }
    }
}