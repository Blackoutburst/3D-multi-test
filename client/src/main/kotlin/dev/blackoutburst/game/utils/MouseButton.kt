package dev.blackoutburst.game.utils

class MouseButton(val id: Int) {

    var isPressed: Boolean = false
    var isReleased: Boolean = false
    var isDown: Boolean = false

    fun reset() {
        this.isReleased = false
        this.isPressed = false
    }
}
