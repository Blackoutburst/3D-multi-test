package dev.blackoutburst.server.network.packets.server

import dev.blackoutburst.server.network.packets.PacketPlayOut
import java.nio.ByteBuffer
import java.nio.ByteOrder

private const val ID: Byte = 0x00

class S00Identification(
    private val entityId: Int,
): PacketPlayOut() {

    init {
        buffer = ByteBuffer.allocate(5).clear()

        buffer?.apply {
            order(ByteOrder.BIG_ENDIAN)
            put(ID)
            putInt(entityId)
        }
    }
}