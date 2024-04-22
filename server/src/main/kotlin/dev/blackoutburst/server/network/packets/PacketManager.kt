package dev.blackoutburst.server.network.packets

import dev.blackoutburst.server.network.packets.client.C00MoveEntity
import java.nio.ByteBuffer

class PacketManager {

    private val packets = mutableMapOf<Int, PacketPlayIn>()

    init {
        packets[0x00] = C00MoveEntity()
    }

    fun read(data: ByteArray) {
        val buffer = ByteBuffer.wrap(data)
        val id = buffer.get().toInt()

        packets[id]?.decode(buffer)
    }

}