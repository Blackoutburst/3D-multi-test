package dev.blackoutburst.game

import dev.blackoutburst.game.core.Camera
import dev.blackoutburst.game.core.Display
import dev.blackoutburst.game.core.UI
import dev.blackoutburst.game.core.entity.EntityManager
import dev.blackoutburst.game.graphics.Color
import dev.blackoutburst.game.maths.Matrix
import dev.blackoutburst.game.maths.Vector2i
import dev.blackoutburst.game.maths.Vector3i
import dev.blackoutburst.game.network.Connection
import dev.blackoutburst.game.utils.Keyboard
import dev.blackoutburst.game.utils.Time
import dev.blackoutburst.game.world.BlockType
import dev.blackoutburst.game.world.World
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11.*
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class Main {

    companion object {
        val osName = System.getProperty("os.name").toString().toLowerCase()
        var blockType = BlockType.GRASS

        val glTaskQueue: ConcurrentLinkedQueue<() -> Unit> = ConcurrentLinkedQueue()

        val window = Display()
            .setFullscreenMode(Display.FullScreenMode.NONE)
            .setTitle("MeinRaft")
            .create()
            .setVSync(osName.contains("mac"))

        var projection = Matrix()
            .projectionMatrix(90f, 1000f, 0.1f)

        val world = World()
        val connection = Connection()
        val camera = Camera()
        val entityManager = EntityManager()

        private var renderPasses = 0
        private var fps = 0
        private var previousTime = Time.runtime
    }

    private fun getFps(): Int {
        val currentTime: Double = Time.runtime
        renderPasses++

        if (currentTime - previousTime >= 1.0) {
            fps = renderPasses
            renderPasses = 0
            previousTime = currentTime
        }
        return (fps)
    }

    private fun setup() {
        window.setClearColor(Color(0.67f, 0.80f, 0.92f))

        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)
        glEnable(GL_DEPTH_TEST)

        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)

        connection.start()

        world.createTextureMaps()
    }

    private fun pollGlTasks() {
        while (glTaskQueue.isNotEmpty()) {
            glTaskQueue.poll()?.invoke()
        }
    }

    fun run() {
        setup()

        while (window.isOpen) {
            pollGlTasks()

            window.clear()
            glEnable(GL_CULL_FACE)
            glEnable(GL_DEPTH_TEST)
            glPolygonMode(GL_FRONT_AND_BACK, UI.renderOption)

            entityManager.update()

            if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_1))
                blockType = BlockType.GRASS
            if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_2))
                blockType = BlockType.DIRT
            if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_3))
                blockType = BlockType.STONE
            if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_4))
                blockType = BlockType.OAK_LOG
            if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_5))
                blockType = BlockType.OAK_LEAVES

            world.render()

            entityManager.render()

            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)

            val result = world.dda(camera.position, camera.getDirection(), 100)
            UI.gameInformation(window.nk.ctx, Vector2i(0), Vector2i(200, 180),
                camera.position,
                result.block?.type ?: BlockType.AIR,
                result.block?.position ?: Vector3i(0)
            )
            UI.renderInformation(window.nk.ctx, Vector2i(Display.getWidth() - 200,0), Vector2i(200, 210),
                getFps(),
                world.chunkUpdate.get(),
                world.blockCount,
                world.chunks.size,
                world.vertexCount
            )

            window.update()
        }

        window.destroy()
        connection.close()
    }
}

fun main() {
    Main().run()
}