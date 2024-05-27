package dev.blackoutburst.game.utils

import org.lwjgl.glfw.GLFW

object Time {
    private const val UPDATE = 1e9f / 20.0

    private var lastTime = System.nanoTime()
    private var init = System.nanoTime()
    private var deltaTime = 0.0

    fun updateDelta() {
        val time = System.nanoTime()
        deltaTime = ((time - lastTime) / 1e9f).toDouble()
        lastTime = time
    }

    fun doUpdate(): Boolean {
        if (System.nanoTime() - init > UPDATE) {
            init += UPDATE.toLong()
            return (true)
        }
        return (false)
    }

    val delta: Double
        get() = (deltaTime)

    val runtime: Double
        get() = (GLFW.glfwGetTime())
}