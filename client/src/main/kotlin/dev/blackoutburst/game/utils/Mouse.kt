package dev.blackoutburst.game.utils

import dev.blackoutburst.game.core.Display
import dev.blackoutburst.game.maths.Vector2f
import org.lwjgl.glfw.GLFW.glfwGetCursorPos
import org.lwjgl.system.MemoryStack
import java.nio.Buffer


object Mouse {
	const val LEFT_BUTTON = 0
	const val RIGHT_BUTTON = 1
	const val MIDDLE_BUTTON = 2

	const val RELEASE = 0
	const val PRESS = 1
	const val DOWN = 2

    var x: Float = 0f
    var y: Float = 0f
	var scroll: Float = 0f
    var position = Vector2f()
	val buttons = mutableMapOf<Int, Int>()

	fun update() {
		buttons.forEach { (key, value) ->
			if (value == PRESS) {
				buttons[key] = DOWN
			}
		}
	}

	fun isButtonPressed(button: Int): Boolean {
		return buttons[button] == PRESS
	}

	fun isButtonReleased(button: Int): Boolean {
		return buttons[button] == RELEASE
	}

	fun isButtonDown(button: Int): Boolean {
		return buttons[button] == DOWN
	}

	fun getRawPosition(): Vector2f {
		val size = Vector2f()

		stack {
			val width = it.mallocDouble(1)
			val height = it.mallocDouble(1)
			glfwGetCursorPos(Display.getWindow(), width, height)
			size.set(width.get().toFloat(), height.get().toFloat())
			(width as Buffer).clear()
			(height as Buffer).clear()
		}
		return (size)
	}
}
