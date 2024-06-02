package dev.blackoutburst.game.network.packets.server

import dev.blackoutburst.game.Main
import dev.blackoutburst.game.main
import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.game.network.packets.PacketPlayIn
import dev.blackoutburst.game.utils.default
import dev.blackoutburst.game.utils.main
import dev.blackoutburst.game.utils.unconfined
import java.nio.ByteBuffer

class S03UpdateEntity(override val size: Int) : PacketPlayIn() {

    override fun decode(buffer: ByteBuffer) {
        val entityId = buffer.getInt()
        val x = buffer.getFloat()
        val y = buffer.getFloat()
        val z = buffer.getFloat()
        val yaw = buffer.getFloat()
        val pitch = buffer.getFloat()

        Main.entityManager.setPosition(entityId, Vector3f(x, y, z))
        Main.entityManager.setRotation(entityId, Vector2f(yaw, pitch))
    }
}