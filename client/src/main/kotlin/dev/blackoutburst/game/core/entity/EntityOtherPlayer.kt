package dev.blackoutburst.game.core.entity

import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.game.maths.Vector3f

class EntityOtherPlayer(
    id: Int,
    position: Vector3f,
    rotation: Vector2f
) : Entity(id, position, rotation) {
    override fun update() {
    }

    override fun render() {
    }

}