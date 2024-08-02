package dev.blackoutburst.server.network.packets.server

import dev.blackoutburst.server.network.packets.PacketPlayOut
import java.nio.ByteBuffer
import java.nio.ByteOrder

private const val ID: Byte = 0x07

class S07UpdateEntityMetadata(
    private val entityId: Int,
    private val name: String,
): PacketPlayOut() {

    init {
        buffer = ByteBuffer.allocate(69).clear()

        buffer?.apply {
            order(ByteOrder.BIG_ENDIAN)
            put(ID)
            putInt(entityId)
            name.encodeToByteArray().forEach { put(it) }
        }
    }
}