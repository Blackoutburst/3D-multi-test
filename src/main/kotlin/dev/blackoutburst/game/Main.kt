package dev.blackoutburst.game

import dev.blackoutburst.game.core.*
import dev.blackoutburst.game.graphics.Color
import dev.blackoutburst.game.graphics.Texture
import dev.blackoutburst.game.maths.Matrix
import dev.blackoutburst.game.world.World
import org.lwjgl.opengl.GL11.*

class Main {
    companion object {
        val window = Window()
            .setFullscreenMode(Window.FullScreenMode.NONE)
            .setTitle("MeinRaft")
            .create()

        val camera = Camera()
        val projection = Matrix()
            .projectionMatrix(90f, 1000f, 0.1f)

        val world = World(50)
        val player = Player(world, camera)
    }

    private fun setup() {
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)
        glEnable(GL_DEPTH_TEST)

        Texture("grass.png")

        world.generate()
    }

    fun run() {
        setup()

        while (window.isOpen) {
            window.clear(Color(173.0f/255.0f, 206.0f/255.0f, 237.0f/255.0f))

            player.update()

            camera.update()

            world.render()

            window.update()
        }

        window.destroy()
    }
}

fun main() {
    Main().run()
}