package dev.blackoutburst.server.network.packets.server

import dev.blackoutburst.server.network.packets.PacketPlayOut
import java.nio.ByteBuffer
import java.nio.ByteOrder

private const val ID: Byte = 0x06

class S06Chat(
    private val message: String,
): PacketPlayOut() {

    init {
        buffer = ByteBuffer.allocate(4097).clear()

        buffer?.apply {
            order(ByteOrder.BIG_ENDIAN)
            put(ID)
            message.encodeToByteArray().forEach { put(it) }
        }
    }
}