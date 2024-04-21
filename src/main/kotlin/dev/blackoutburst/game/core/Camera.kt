package dev.blackoutburst.game.core

import dev.blackoutburst.game.core.Keyboard.isKeyDown
import dev.blackoutburst.game.maths.Matrix
import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.game.maths.Vector3f
import kotlin.math.cos
import kotlin.math.sin


class Camera {
    var position = Vector3f(0f, 20f, 0f)
    var rotation = Vector2f(0f)
    val hitbox = Vector3f(0.2f, 1.8f, 0.2f)
    var velocity = Vector3f()
    val runSpeed = 0.0075f
    val walkSpeed = 0.005f
    var moving = false
    val gravity = -0.005f
    val drag = 0.90f
    var isJumping = false
    var jumpPower = 1.5f

    var view = Matrix()
        .translate(position)

    private var lastMousePosition: Vector2f = Mouse.getRawPosition()

    private val sensitivity = 0.1f

    private fun rotate() {
        val mousePosition: Vector2f = Mouse.getRawPosition()

        var xOffset = mousePosition.x - lastMousePosition.x
        var yOffset = mousePosition.y - lastMousePosition.y

        lastMousePosition = mousePosition.copy()

        xOffset *= sensitivity
        yOffset *= sensitivity


        rotation.x += xOffset
        rotation.y += yOffset

        if (rotation.y > 89.0f) rotation.y = 89.0f
        if (rotation.y < -89.0f) rotation.y = -89.0f
    }

    private fun collide(): Boolean {
        val playerMin = position - Vector3f(0f, hitbox.y/4f, 0f)
        val playerMax = position + Vector3f(hitbox.x * 4f, hitbox.y/4f, hitbox.z * 4f)

        for (block in Main.blocks) {
            val blockMin = block.position
            val blockMax = block.position + Vector3f(1f)

            if (playerMin.x <= blockMax.x && playerMax.x >= blockMin.x &&
                playerMin.y <= blockMax.y && playerMax.y >= blockMin.y &&
                playerMin.z <= blockMax.z && playerMax.z >= blockMin.z) {
                return true
            }
        }
        return false
    }

    private fun grounded(): Boolean {
        val playerMin = position - Vector3f(0f, hitbox.y/2f, 0f)
        val playerMax = position + Vector3f(hitbox.x * 4f, hitbox.y/2f, hitbox.z * 4f)

        val playerFeetY = playerMin.y - 0.1f

        for (block in Main.blocks) {
            val blockMin = block.position
            val blockMax = block.position + Vector3f(1f)

            if (playerMin.x < blockMax.x && playerMax.x > blockMin.x &&
                playerFeetY < blockMax.y && playerMin.y >= blockMin.y &&
                playerMin.z < blockMax.z && playerMax.z > blockMin.z) {
                return true
            }
        }
        return false
    }


    private fun move() {
        moving = false

        var potentialX = position.x
        var potentialZ = position.z

        if (isKeyDown(Keyboard.W)) {
            velocity.x -= sin(-rotation.x * Math.PI / 180).toFloat()
            velocity.z -= cos(-rotation.x * Math.PI / 180).toFloat()
            moving = true
        }

        if (isKeyDown(Keyboard.S)) {
            velocity.x += sin(-rotation.x * Math.PI / 180).toFloat()
            velocity.z += cos(-rotation.x * Math.PI / 180).toFloat()
            moving = true
        }

        if (isKeyDown(Keyboard.A)) {
            velocity.x += sin((-rotation.x - 90) * Math.PI / 180).toFloat()
            velocity.z += cos((-rotation.x - 90) * Math.PI / 180).toFloat()
            moving = true
        }

        if (isKeyDown(Keyboard.D)) {
            velocity.x += sin((-rotation.x + 90) * Math.PI / 180).toFloat()
            velocity.z += cos((-rotation.x + 90) * Math.PI / 180).toFloat()
            moving = true
        }

        val speed = if (isKeyDown(Keyboard.LEFT_CONTROL)) runSpeed else walkSpeed

        if (moving) {
            val horizontalVelocity = Vector3f(velocity.x, 0f, velocity.z)

            velocity.x = horizontalVelocity.normalize().x
            velocity.z = horizontalVelocity.normalize().z
        } else {
            velocity.x *= drag
            velocity.z *= drag
        }

        potentialX += (velocity.x * speed * Time.delta.toFloat())
        potentialZ += (velocity.z * speed * Time.delta.toFloat())

        val oldPosition = Vector3f(position.x, position.y, position.z)
        position.x = potentialX
        if (collide()) position.x = oldPosition.x
        position.z = potentialZ
        if (collide()) position.z = oldPosition.z

        if (isKeyDown(Keyboard.SPACE) && grounded()) {
            isJumping = true
            velocity.y = jumpPower
        }

        if (!grounded() || isJumping) {
            velocity.y += gravity * Time.delta.toFloat()
            isJumping = false
        } else {
            velocity.y = 0f
        }

        if (position.y < -50f) {
            position.y = 20f
            velocity.y = 0f
        }

        position.y += (velocity.y * Time.delta.toFloat() * 0.005f)
    }

    fun update() {
        if (!Main.showCursor) {
            rotate()
            move()
        }

        view.setIdentity()
            .rotate(Math.toRadians(rotation.y.toDouble()).toFloat(), Vector3f(1f, 0f, 0f))
            .rotate(Math.toRadians(rotation.x.toDouble()).toFloat(), Vector3f(0f, 1f, 0f))
            .translate(Vector3f(-position.x, -position.y, -position.z))
    }
}
