package dev.blackoutburst.server.network.packets.server

import dev.blackoutburst.server.maths.Vector3i
import dev.blackoutburst.server.network.packets.PacketPlayOut
import java.nio.ByteBuffer
import java.nio.ByteOrder

private const val ID: Byte = 0x05

class S05SendMonoTypeChunk(
    private val position: Vector3i,
    private val type: Byte
): PacketPlayOut() {

    init {
        buffer = ByteBuffer.allocate(14).clear()

        buffer?.apply {
            order(ByteOrder.BIG_ENDIAN)
            put(ID)
            putInt(position.x)
            putInt(position.y)
            putInt(position.z)
            put(type)
        }
    }
}