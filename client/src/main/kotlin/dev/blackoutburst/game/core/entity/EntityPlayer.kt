package dev.blackoutburst.game.core.entity

import dev.blackoutburst.game.Main
import dev.blackoutburst.game.core.Window
import dev.blackoutburst.game.graphics.Color
import dev.blackoutburst.game.graphics.Cube
import dev.blackoutburst.game.graphics.WorldBlock
import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.game.maths.Vector3i
import dev.blackoutburst.game.network.Connection
import dev.blackoutburst.game.network.packets.client.C00UpdateEntity
import dev.blackoutburst.game.network.packets.client.C01UpdateBlock
import dev.blackoutburst.game.utils.Keyboard
import dev.blackoutburst.game.utils.Keyboard.isKeyDown
import dev.blackoutburst.game.utils.Mouse
import dev.blackoutburst.game.utils.Time
import dev.blackoutburst.game.world.BlockType
import dev.blackoutburst.game.world.Chunk
import dev.blackoutburst.game.world.World
import kotlin.math.cos
import kotlin.math.sin

class EntityPlayer(
    id: Int,
    position: Vector3f = Vector3f(0f, 50f, 0f),
    rotation: Vector2f = Vector2f(),
    private val world: World,
    private val connection: Connection,
): Entity(id, position, rotation) {
    private var vHold = false

    private var renderBoundingBox = false
    private val boundingBox = Cube(Vector3f(), Vector2f(), Color(0.2f, 0.8f, 0.1f, 0.6f))
    private var flying = true
    private val hitbox = Vector3f(0.15f, 1.8f, 0.15f)
    private var velocity = Vector3f()
    private val runSpeed = 8f
    private val walkSpeed = 5f
    private var moving = false
    private val gravity = -40f
    private var isJumping = false
    private var jumpPower = 10f
    private var sprint = false

    private var lastMousePosition: Vector2f = Mouse.getRawPosition()
    private val sensitivity = 0.1f

    override fun update() {
        networkUpdate()
        if (Window.showCursor) return
        mouseAction()
        rotate()
        move()
        updateCamera()

        val result = world.dda(Main.camera.position, Main.camera.getDirection(), 20)
        result.block?.position?.let { b ->
            result.face?.let { f ->
                boundingBox.position = Vector3f(
                    b.x.toFloat() + f.x.toFloat(),
                    b.y.toFloat() + f.y.toFloat(),
                    b.z.toFloat() + f.z.toFloat()
                ) + Vector3f(0.5f)
                renderBoundingBox = true
            }
        } ?: run {
            renderBoundingBox = false
        }
    }

    override fun render() {
        if (renderBoundingBox)
            boundingBox.draw()
    }

    private fun networkUpdate() {
        if (Time.doUpdate()) {
            connection.write(C00UpdateEntity(id, position, rotation))
        }
    }

    private fun updateCamera() {
        Main.camera.position = position
        Main.camera.rotation = rotation
        Main.camera.update()
    }

    private fun collide(): Boolean {
        val playerMin = position - Vector3f(-hitbox.x * 2f, hitbox.y/2f, -hitbox.z * 2f) - Vector3f(0.5f)
        val playerMax = position + Vector3f(hitbox.x * 5f, hitbox.y/2f, hitbox.z * 5f) - Vector3f(0.5f)

        for (chunk in world.getCloseChunk(position)) {
            for (i in 0 until chunk.blocks.size) {
                val blockMin = chunk.indexToXYZ(i)
                val blockMax = blockMin + Vector3i(1)

                if (playerMin.x <= blockMax.x && playerMax.x >= blockMin.x &&
                    playerMin.y <= blockMax.y && playerMax.y >= blockMin.y &&
                    playerMin.z <= blockMax.z && playerMax.z >= blockMin.z &&
                    chunk.blocks[i] != BlockType.AIR.id
                ) {
                    return true
                }
            }
        }
        return false
    }

    private fun grounded(): Boolean {
        val playerMin = position - Vector3f(-hitbox.x * 2f, hitbox.y/2f, -hitbox.z * 2f) - Vector3f(0.5f)
        val playerMax = position + Vector3f(hitbox.x * 5f, hitbox.y/2f, hitbox.z * 5f) - Vector3f(0.5f)

        val playerFeetY = playerMin.y - 0.1f

        for (chunk in world.getCloseChunk(position)) {
            for (i in 0 until chunk.blocks.size) {
                val blockMin = chunk.indexToXYZ(i)
                val blockMax = blockMin + Vector3i(1)

                if (playerMin.x < blockMax.x && playerMax.x > blockMin.x &&
                    playerFeetY < blockMax.y && playerMin.y >= blockMin.y &&
                    playerMin.z < blockMax.z && playerMax.z > blockMin.z &&
                    chunk.blocks[i] != BlockType.AIR.id
                ) {
                    return true
                }
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

        if (isKeyDown(Keyboard.V) && !vHold)
            flying = !flying
        vHold = isKeyDown(Keyboard.V)

        var potentialX = position.x
        var potentialZ = position.z
        var potentialY = position.y

        if (isKeyDown(Keyboard.LEFT_CONTROL)) {
            sprint = true
        }

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

        val speed = if (sprint) runSpeed else walkSpeed


        if (moving) {
            val horizontalVelocity = Vector3f(velocity.x, 0f, velocity.z)

            velocity.x = horizontalVelocity.normalize().x
            velocity.z = horizontalVelocity.normalize().z
        } else {
            sprint = false
        }

        potentialX += velocity.x * Time.delta.toFloat() * speed
        potentialZ += velocity.z * Time.delta.toFloat() * speed

        val oldPosition = Vector3f(position.x, position.y, position.z)
        position.x = potentialX
        if (collide() && !flying) position.x = oldPosition.x
        position.z = potentialZ
        if (collide() && !flying) position.z = oldPosition.z


        if (isKeyDown(Keyboard.SPACE) && grounded()) {
            isJumping = true
            velocity.y = jumpPower
        }

        if ((!grounded() || isJumping) && !flying) {
            velocity.y += gravity * Time.delta.toFloat()
            isJumping = false
        } else {
            velocity.y = 0f
        }


        potentialY += velocity.y * Time.delta.toFloat()
        position.y = potentialY
        if (collide() && !flying) {
            position.y = oldPosition.y
            velocity.y = 0f
            isJumping = false
        }

        if (position.y < -50f) {
            position.y = 50f
            velocity.y = 0f
        }

        if (isKeyDown(Keyboard.SPACE) && flying) {
            position.y += 1 * speed * Time.delta.toFloat()
        }

        if (isKeyDown(Keyboard.LEFT_SHIFT) && flying) {
            position.y -= 1 * speed * Time.delta.toFloat()
        }

        velocity.x = 0f
        velocity.z = 0f

    }

    private fun mouseAction() {
        if (Mouse.rightButton.isPressed) {
            val result = world.dda(Main.camera.position, Main.camera.getDirection(), 20)
            result.block?.let { b ->
                result.face?.let { f ->
                    connection.write(C01UpdateBlock(Main.blockType.id, b.position + f))
                }
            }
        }

        if (Mouse.leftButton.isPressed) {
            world.dda(Main.camera.position, Main.camera.getDirection(), 20)
                .block?.let {
                    connection.write(C01UpdateBlock(BlockType.AIR.id, it.position))
                }
        }
    }
}