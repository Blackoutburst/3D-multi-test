package dev.blackoutburst.game.network.packets.client

import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.game.network.packets.PacketPlayOut
import java.nio.ByteBuffer
import java.nio.ByteOrder

private const val ID: Byte = 0x00

class C00MoveEntity(
    private val position: Vector3f
) : PacketPlayOut() {

    init {
        buffer.apply {
            order(ByteOrder.BIG_ENDIAN)
            put(ID)
            putFloat(position.x)
            putFloat(position.y)
            putFloat(position.z)
        }
    }
}