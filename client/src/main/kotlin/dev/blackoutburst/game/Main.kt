package dev.blackoutburst.game

import dev.blackoutburst.game.core.Camera
import dev.blackoutburst.game.core.Display
import dev.blackoutburst.game.core.UI
import dev.blackoutburst.game.core.entity.EntityManager
import dev.blackoutburst.game.graphics.Color
import dev.blackoutburst.game.graphics.TextureArray
import dev.blackoutburst.game.maths.Matrix
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.game.network.Connection
import dev.blackoutburst.game.utils.Keyboard
import dev.blackoutburst.game.utils.Keyboard.isKeyDown
import dev.blackoutburst.game.utils.Textures
import dev.blackoutburst.game.utils.Time
import dev.blackoutburst.game.world.BlockType
import dev.blackoutburst.game.world.World
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

        //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
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

            entityManager.update()

            if (isKeyDown(Keyboard.NUM1))
                blockType = BlockType.GRASS
            if (isKeyDown(Keyboard.NUM2))
                blockType = BlockType.DIRT
            if (isKeyDown(Keyboard.NUM3))
                blockType = BlockType.STONE
            if (isKeyDown(Keyboard.NUM4))
                blockType = BlockType.OAK_LOG
            if (isKeyDown(Keyboard.NUM5))
                blockType = BlockType.OAK_LEAVES

            world.render()

            entityManager.render()

            UI.playerPosition(window.nk.ctx, camera.position, getFps())

            window.update()
        }


        window.destroy()
        connection.close()
    }
}

fun main() {
    Main().run()
}