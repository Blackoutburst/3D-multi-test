package dev.blackoutburst.game.core.entity

import dev.blackoutburst.game.graphics.Color
import dev.blackoutburst.game.graphics.Cube
import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.game.maths.Vector3f

class EntityOtherPlayer(
    id: Int,
    position: Vector3f = Vector3f(),
    rotation: Vector2f = Vector2f(),
) : Entity(id, position, rotation) {

    private val cube = Cube(position, rotation, Color(0.8f, 0.2f, 0.6f))

    override fun update() {
        cube.position = position
        cube.rotation = rotation
    }

    override fun render() {
        cube.draw()
    }

}