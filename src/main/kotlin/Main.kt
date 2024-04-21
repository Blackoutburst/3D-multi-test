import dev.blackoutburst.game.core.*
import dev.blackoutburst.game.graphics.Color
import dev.blackoutburst.game.graphics.Block
import dev.blackoutburst.game.graphics.Texture
import dev.blackoutburst.game.maths.Matrix
import dev.blackoutburst.game.maths.Vector3f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11.*
import kotlin.math.sign

data class HitResult(val block: Block?, val face: Vector3f?)

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

    private fun getBlockAt(position: Vector3f): HitResult {
        blocks.find { block ->
            block.position.x - 0.5 <= position.x && block.position.x + 0.5 >= position.x &&
                    block.position.y - 0.5 <= position.y && block.position.y + 0.5 >= position.y &&
                    block.position.z - 0.5 <= position.z && block.position.z + 0.5 >= position.z
        }?.let { block ->
            // Determine which face is hit
            val dx = maxOf(position.x - (block.position.x + 0.5), block.position.x - 0.5 - position.x)
            val dy = maxOf(position.y - (block.position.y + 0.5), block.position.y - 0.5 - position.y)
            val dz = maxOf(position.z - (block.position.z + 0.5), block.position.z - 0.5 - position.z)

            val face = when {
                dx > dy && dx > dz -> Vector3f(sign(position.x - block.position.x), 0f, 0f)
                dy > dz -> Vector3f(0f, sign(position.y - block.position.y), 0f)
                else -> Vector3f(0f, 0f, sign(position.z - block.position.z))
            }

            return HitResult(block, face)
        }
        return HitResult(null, null)
    }


    private fun rayCast(start: Vector3f, direction: Vector3f, distance: Float): HitResult {
        var currentPosition = start
        val step = direction.normalize() * 0.005f

        for (i in 0 until (distance / step.length()).toInt()) {
            val hitResult = getBlockAt(currentPosition)
            if (hitResult.block != null) {
                return hitResult
            }
            currentPosition += step
        }
        return HitResult(null, null)
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
        val mapSize = 20

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

            if (Mouse.rightButton.isPressed) {
                val result = rayCast(camera.position, camera.getDirection(), 5f)
                result.block?.let { block ->
                    result.face?.let { face ->
                        if (getBlockAt(block.position + face).block == null) {
                            blocks.add(
                                Block(
                                    block.position + face
                                )
                            )
                        }
                    }
                }
                Block.setOffset(blocks)
            }

            if (Mouse.leftButton.isPressed) {
                val result = rayCast(camera.position, camera.getDirection(), 5f)
                blocks.remove(result.block)
                Block.setOffset(blocks)
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