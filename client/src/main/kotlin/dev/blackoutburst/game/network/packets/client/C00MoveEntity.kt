package dev.blackoutburst.game.network.packets.client

import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.game.network.packets.PacketPlayOut
import java.nio.ByteOrder
import java.util.zip.Deflater
import java.util.zip.Inflater

private const val ID: Byte = 0x00

class C00MoveEntity(
    private val entityId: Int,
    private val position: Vector3f
) : PacketPlayOut() {

    init {
        buffer.apply {
            order(ByteOrder.BIG_ENDIAN)
            put(ID)
            putInt(entityId)
            putFloat(position.x)
            putFloat(position.y)
            putFloat(position.z)
        }
    }
}