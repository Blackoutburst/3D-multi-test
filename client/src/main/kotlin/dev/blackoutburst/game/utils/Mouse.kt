package dev.blackoutburst.game.utils

import dev.blackoutburst.game.core.Display
import dev.blackoutburst.game.maths.Vector2f
import org.lwjgl.glfw.GLFW.glfwGetCursorPos
import org.lwjgl.system.MemoryStack
import java.nio.Buffer


object Mouse {
    var x: Float = 0f
    var y: Float = 0f
	var scroll: Float = 0f
    var position = Vector2f()
	val leftButton: MouseButton = MouseButton(0)
	val rightButton: MouseButton = MouseButton(1)
	val middleButton: MouseButton = MouseButton(2)

	fun getRawPosition(): Vector2f {
		val size = Vector2f()

		try {
			MemoryStack.stackPush().use { stack ->
				val width = stack.mallocDouble(1)
				val height = stack.mallocDouble(1)
				glfwGetCursorPos(Display.getWindow(), width, height)
				size.set(width.get().toFloat(), height.get().toFloat())
				(width as Buffer).clear()
				(height as Buffer).clear()
			}
		} catch (e: Exception) {
			System.err.println("Error while getting cursor position: $e")
		}
		return (size)
	}
}
