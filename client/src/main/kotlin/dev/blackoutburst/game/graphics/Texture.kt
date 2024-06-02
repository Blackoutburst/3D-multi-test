package dev.blackoutburst.game.graphics

import dev.blackoutburst.game.utils.IOUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import org.lwjgl.opengl.GL30
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
            id = GL11.glGenTextures()
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id)

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
            GL11.glTexImage2D(
                GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA,
                width!![0],
                height!![0], 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data
            )
            GL11.glTexParameteri(GL30.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_NEAREST)
            GL11.glTexParameteri(GL30.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
            GL11.glTexParameteri(GL30.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_REPEAT)
            GL11.glTexParameteri(GL30.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_REPEAT)
            (comp as Buffer).clear()
        }
        data?.let { STBImage.stbi_image_free(it) }
    }

}
