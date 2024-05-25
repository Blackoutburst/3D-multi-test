package dev.blackoutburst.server.network.packets.server

import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.server.network.packets.PacketPlayOut
import java.nio.ByteBuffer
import java.nio.ByteOrder

private const val ID: Byte = 0x01

class S01AddEntity(
    private val entityId: Int,
    private val position: Vector3f,
    private val rotation: Vector2f
): PacketPlayOut() {

    init {
        buffer = ByteBuffer.allocate(25).clear()

        buffer?.apply {
            order(ByteOrder.BIG_ENDIAN)
            put(ID)
            putInt(entityId)
            putFloat(position.x)
            putFloat(position.y)
            putFloat(position.z)
            putFloat(rotation.x)
            putFloat(rotation.y)
        }
    }
}