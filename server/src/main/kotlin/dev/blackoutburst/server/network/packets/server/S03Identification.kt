package dev.blackoutburst.server.network.packets.server

import dev.blackoutburst.server.network.packets.PacketPlayOut
import java.nio.ByteOrder

private const val ID: Byte = 0x03

class S03Identification(
    private val entityId: Int,
): PacketPlayOut() {

    init {
        buffer.apply {
            order(ByteOrder.BIG_ENDIAN)
            put(ID)
            putInt(entityId)
        }
    }
}