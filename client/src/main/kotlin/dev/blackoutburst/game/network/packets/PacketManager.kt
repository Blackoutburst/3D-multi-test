package dev.blackoutburst.game.network.packets

import dev.blackoutburst.game.network.packets.server.S00MoveEntity
import java.nio.ByteBuffer

class PacketManager {

    private val packets = mutableMapOf<Int, PacketPlayIn>()

    init {
        packets[0x00] = S00MoveEntity()
    }

    fun read(data: ByteArray) {
        val buffer = ByteBuffer.wrap(data)
        val id = buffer.get().toInt()

        packets[id]?.decode(buffer)
    }

}