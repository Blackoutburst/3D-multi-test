package dev.blackoutburst.game.network.packets.client

import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.game.network.packets.PacketPlayOut
import java.nio.ByteOrder

private const val ID: Byte = 0x01

class C01EntityRotation(
    private val entityId: Int,
    private val rotation: Vector2f
) : PacketPlayOut() {

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