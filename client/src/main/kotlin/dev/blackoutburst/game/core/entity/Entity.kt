package dev.blackoutburst.game.core.entity

import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.game.maths.Vector3f

abstract class Entity(
    val id: Int,
    var position: Vector3f,
    var rotation: Vector2f
) {

    abstract fun update()
    abstract fun render()
}