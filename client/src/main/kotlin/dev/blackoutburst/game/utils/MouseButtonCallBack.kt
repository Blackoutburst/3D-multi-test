package dev.blackoutburst.game.utils

import org.lwjgl.glfw.GLFWMouseButtonCallbackI

class MouseButtonCallBack : GLFWMouseButtonCallbackI {
    override fun invoke(window: Long, button: Int, action: Int, mods: Int) {
        when (button) {
            0 -> {
                Mouse.leftButton.isPressed = action == 1
                Mouse.leftButton.isDown = action == 1
                Mouse.leftButton.isReleased = action == 0
            }

            1 -> {
                Mouse.rightButton.isPressed = action == 1
                Mouse.rightButton.isDown = action == 1
                Mouse.rightButton.isReleased = action == 0
            }

            2 -> {
                Mouse.middleButton.isPressed = action == 1
                Mouse.middleButton.isDown = action == 1
                Mouse.middleButton.isReleased = action == 0
            }
        }
    }
}
