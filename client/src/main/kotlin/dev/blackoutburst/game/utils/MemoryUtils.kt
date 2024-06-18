package dev.blackoutburst.game.utils

import org.lwjgl.system.MemoryStack

inline fun <R> stack(block: (MemoryStack) -> R): R {
    val stack = MemoryStack.stackPush()
    stack.use { return block(it) }
}