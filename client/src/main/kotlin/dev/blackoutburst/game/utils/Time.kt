package dev.blackoutburst.game.utils

import org.lwjgl.glfw.GLFW

object Time {
    private var lastTime = System.nanoTime()
    private val update = 1e9f / 5.0
    private var init = System.nanoTime()

    private var deltaTime = 0.0

    internal fun updateDelta() {
        val time = System.nanoTime()
        deltaTime = ((time - lastTime) / 1e9f).toDouble()
        lastTime = time
    }

    fun doUpdate(): Boolean {
        if (System.nanoTime() - init > update) {
            init += update.toLong()
            return (true)
        }
        return (false)
    }

    val delta: Double
        get() = (deltaTime)

    val runtime: Double
        get() = (GLFW.glfwGetTime())
}