package dev.blackoutburst.game.utils

import org.lwjgl.glfw.GLFWScrollCallbackI

class MouseScrollCallBack : GLFWScrollCallbackI {
    override fun invoke(window: Long, xOffset: Double, yOffset: Double) {
        Mouse.scroll = (Mouse.scroll + yOffset).toFloat()
    }
}
