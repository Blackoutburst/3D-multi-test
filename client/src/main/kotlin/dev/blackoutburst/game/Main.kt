package dev.blackoutburst.game

import dev.blackoutburst.game.core.Camera
import dev.blackoutburst.game.core.Window
import dev.blackoutburst.game.core.entity.EntityManager
import dev.blackoutburst.game.graphics.Color
import dev.blackoutburst.game.graphics.Cube
import dev.blackoutburst.game.graphics.Texture
import dev.blackoutburst.game.maths.Matrix
import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.game.network.Connection
import dev.blackoutburst.game.utils.Time
import dev.blackoutburst.game.world.World
import org.lwjgl.opengl.GL11.*
import java.util.concurrent.ConcurrentLinkedQueue

class Main {

    companion object {
        val glTaskQueue: ConcurrentLinkedQueue<() -> Unit> = ConcurrentLinkedQueue()

        val window = Window()
            .setFullscreenMode(Window.FullScreenMode.NONE)
            .setTitle("MeinRaft")
            .create()

        val projection = Matrix()
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
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)
        glEnable(GL_DEPTH_TEST)

        Texture("grass.png")

        connection.start()
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

            window.clear(Color(0.67f, 0.80f, 0.92f))

            entityManager.update()

            world.render()

            entityManager.render()

            println(getFps())

            window.update()

        }

        window.destroy()
        connection.close()
    }
}

fun main() {
    Main().run()
}