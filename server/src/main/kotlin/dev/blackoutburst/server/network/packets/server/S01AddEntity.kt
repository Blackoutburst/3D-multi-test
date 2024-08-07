package dev.blackoutburst.server.network.packets.server

import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.server.network.packets.PacketPlayOut
import dev.blackoutburst.server.utils.fit
import java.nio.ByteBuffer
import java.nio.ByteOrder

private const val ID: Byte = 0x01

class S01AddEntity(
    private val entityId: Int,
    private val position: Vector3f,
    private val rotation: Vector2f,
    private val name: String
): PacketPlayOut() {

    init {
        buffer = ByteBuffer.allocate(89).clear()

        buffer?.apply {
            order(ByteOrder.BIG_ENDIAN)
            put(ID)
            putInt(entityId)
            putFloat(position.x)
            putFloat(position.y)
            putFloat(position.z)
            putFloat(rotation.x)
            putFloat(rotation.y)
            name.fit(64).encodeToByteArray().forEach { put(it) }
        }
    }
}