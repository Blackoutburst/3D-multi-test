package dev.blackoutburst.server.network.packets.server

import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.server.network.packets.PacketPlayOut
import java.nio.ByteOrder

private const val ID: Byte = 0x04

class S04EntityRotation(
    private val entityId: Int,
    private val rotation: Vector2f
): PacketPlayOut() {

    init {
        buffer.apply {
            order(ByteOrder.BIG_ENDIAN)
            put(ID)
            putInt(entityId)
            putFloat(rotation.x)
            putFloat(rotation.y)
        }
    }
}