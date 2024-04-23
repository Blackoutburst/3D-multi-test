package dev.blackoutburst.game.core.entity

import dev.blackoutburst.game.Main
import dev.blackoutburst.game.core.Camera
import dev.blackoutburst.game.graphics.Block
import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.game.network.Connection
import dev.blackoutburst.game.network.packets.client.C00MoveEntity
import dev.blackoutburst.game.utils.Keyboard
import dev.blackoutburst.game.utils.Keyboard.isKeyDown
import dev.blackoutburst.game.utils.Mouse
import dev.blackoutburst.game.utils.Time
import dev.blackoutburst.game.world.World
import kotlin.math.cos
import kotlin.math.sin

class EntityPlayer(
    id: Int,
    position: Vector3f,
    private val world: World,
    private val connection: Connection,
): Entity(id, position) {
    private var rotation = Vector2f()
    private val hitbox = Vector3f(0.15f, 1.8f, 0.15f)
    private var velocity = Vector3f()
    private val runSpeed = 0.0075f
    private val walkSpeed = 0.005f
    private var moving = false
    private val gravity = -0.004f
    private val drag = 0.90f
    private var isJumping = false
    private var jumpPower = 1.1f

    private var lastMousePosition: Vector2f = Mouse.getRawPosition()
    private val sensitivity = 0.1f

    override fun update() {
        mouseAction()
        rotate()
        move()
        updateCamera()
        networkUpdate()
    }

    override fun render() {
    }

    private fun networkUpdate() {
        connection.write(C00MoveEntity(id, position))
    }

    private fun updateCamera() {
        Main.camera.position = position
        Main.camera.rotation = rotation
        Main.camera.update()
    }

    private fun collide(): Boolean {
        val playerMin = position - Vector3f(-hitbox.x * 2f, hitbox.y/4f, -hitbox.z * 2f)
        val playerMax = position + Vector3f(hitbox.x * 5f, hitbox.y/2f, hitbox.z * 5f)

        for (block in world.blocks) {
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
        val playerMin = position - Vector3f(-hitbox.x * 2f, hitbox.y/2f, -hitbox.z * 2f)
        val playerMax = position + Vector3f(hitbox.x * 5f, hitbox.y/2f, hitbox.z * 5f)

        val playerFeetY = playerMin.y - 0.1f

        for (block in world.blocks) {
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

    private fun move() {
        moving = false

        var potentialX = position.x
        var potentialZ = position.z
        var potentialY = position.y

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

        potentialY += (velocity.y * Time.delta.toFloat() * 0.005f)
        position.y = potentialY
        if (collide()) {
            position.y = oldPosition.y
            velocity.y = 0f
            isJumping = false
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

    private fun mouseAction() {
        if (Mouse.rightButton.isPressed) {
            val result = world.rayCast(Main.camera.position, Main.camera.getDirection(), 5f)
            result.block?.let { b ->
                result.face?.let { f ->
                    world.placeBlock(Block(
                        position = b.position + f
                    ))
                }
            }
        }

        if (Mouse.leftButton.isPressed) {
            world.rayCast(Main.camera.position, Main.camera.getDirection(), 5f)
                .block?.let {
                    world.destroyBlock(it)
                }
        }
    }
}