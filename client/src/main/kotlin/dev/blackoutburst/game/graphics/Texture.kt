package dev.blackoutburst.game.graphics

import dev.blackoutburst.game.utils.IOUtils
import org.lwjgl.opengl.GL30.*
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.IntBuffer

class Texture(filePath: String, channels: Int = 0) {
    var id: Int = 0

    private var width: IntBuffer? = null
    private var height: IntBuffer? = null

    init {
        var data: ByteBuffer?
        MemoryStack.stackPush().use { stack ->
            id = glGenTextures()
            glBindTexture(GL_TEXTURE_2D, id)

            val comp: IntBuffer = stack.mallocInt(1)
            width = stack.mallocInt(1)
            height = stack.mallocInt(1)

            data =
                width?.let { w ->
                    height?.let { h ->
                        STBImage.stbi_load_from_memory(
                            IOUtils.ioResourceToByteBuffer(filePath, 1024),
                            w,
                            h,
                            comp,
                            channels
                        )
                    }
                }
            glTexImage2D(
                GL_TEXTURE_2D, 0, GL_RGBA,
                width!![0],
                height!![0], 0, GL_RGBA, GL_UNSIGNED_BYTE, data
            )
            glGenerateMipmap(GL_TEXTURE_2D)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER)
            (comp as Buffer).clear()
        }
        data?.let { STBImage.stbi_image_free(it) }
    }

}
