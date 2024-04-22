package dev.blackoutburst.game.utils

import org.lwjgl.glfw.GLFW

object Time {
    private var init = System.nanoTime()
    private var lastTime = System.nanoTime()

    private var deltaTime = 0.0

    private const val UPDATE = 1000000000.0 / 60.0

    internal fun updateDelta() {
        val time = System.nanoTime()
        deltaTime = ((time - lastTime) / 1000000.0f).toDouble()
        lastTime = time
    }

    val delta: Double
        get() = (deltaTime)

    val runtime: Double
        get() = (GLFW.glfwGetTime())
}