import dev.blackoutburst.game.core.*
import dev.blackoutburst.game.graphics.Color
import dev.blackoutburst.game.graphics.Block
import dev.blackoutburst.game.graphics.Texture
import dev.blackoutburst.game.maths.Matrix
import dev.blackoutburst.game.maths.Vector3f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11.*


class Main {
    companion object {
        var showCursor = false
        val blocks = mutableListOf<Block>()

        val window = Window()
            .setFullscreenMode(Window.FullScreenMode.NONE)
            .setTitle("MeinRaft")
            .create()

        val camera = Camera()
        val projection = Matrix()
            .projectionMatrix(90f, 1000f, 0.1f)
    }

    fun run() {
        var toggleMousePressed = false

        GLFW.glfwSetInputMode(Window.id, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED)

        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)
        glEnable(GL_DEPTH_TEST)

        Texture("./grass.png")
        val mapSize = 200

        for (x in -mapSize until mapSize) {
            for (z in -mapSize until mapSize) {
                blocks.add(Block(
                    position = Vector3f(x.toFloat(), (OpenSimplex2.noise2(8, x / 100.0, z / 100.0) * 7).toInt().toFloat(), z.toFloat()),
                ))
            }
        }

        Block.setOffset(blocks)

        while (window.isOpen) {
            window.clear(Color(173.0f/255.0f, 206.0f/255.0f, 237.0f/255.0f))

            if (Keyboard.isKeyDown(Keyboard.ESCAPE)) {
                window.close()
            }

            if (Keyboard.isKeyDown(Keyboard.LEFT_ALT) && !toggleMousePressed) {
                showCursor = !showCursor
                GLFW.glfwSetInputMode(Window.id, GLFW.GLFW_CURSOR, if (showCursor) GLFW.GLFW_CURSOR_NORMAL else GLFW.GLFW_CURSOR_DISABLED)
            }
            toggleMousePressed = Keyboard.isKeyDown(Keyboard.LEFT_ALT)

            camera.update()

            Block.draw(blocks.size)

            window.update()
        }

        window.destroy()
    }
}

fun main() {
    Main().run()
}