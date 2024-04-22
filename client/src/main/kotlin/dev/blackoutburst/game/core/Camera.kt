package dev.blackoutburst.game.core

import dev.blackoutburst.game.maths.Matrix
import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.game.maths.Vector3f
import kotlin.math.cos
import kotlin.math.sin


class Camera {
    var position = Vector3f(0f, 20f, 0f)
    var rotation = Vector2f(0f)

    var view = Matrix()
        .translate(position)


    fun update() {
        view.setIdentity()
            .rotate(Math.toRadians(rotation.y.toDouble()).toFloat(), Vector3f(1f, 0f, 0f))
            .rotate(Math.toRadians(rotation.x.toDouble()).toFloat(), Vector3f(0f, 1f, 0f))
            .translate(Vector3f(-position.x, -position.y, -position.z))
    }

    fun getDirection(): Vector3f {
        val radianYaw = Math.toRadians(rotation.x.toDouble() - 90).toFloat()
        val radianPitch = Math.toRadians(-rotation.y.toDouble()).toFloat()

        val x = cos(radianPitch) * cos(radianYaw)
        val y = sin(radianPitch)
        val z = cos(radianPitch) * sin(radianYaw)

        return Vector3f(x, y, z).normalize()
    }
}
