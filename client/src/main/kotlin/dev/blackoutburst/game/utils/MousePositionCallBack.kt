package dev.blackoutburst.game.utils

import dev.blackoutburst.game.core.Display
import dev.blackoutburst.game.maths.Vector2f
import org.lwjgl.glfw.GLFWCursorPosCallbackI

class MousePositionCallBack : GLFWCursorPosCallbackI {
    override fun invoke(window: Long, xPos: Double, yPos: Double) {
        Mouse.x = xPos.toFloat()
        Mouse.y = (Display.getHeight() - yPos).toFloat()
        Mouse.position = Vector2f(xPos.toFloat(), (Display.getHeight() - yPos).toFloat())
    }
}
