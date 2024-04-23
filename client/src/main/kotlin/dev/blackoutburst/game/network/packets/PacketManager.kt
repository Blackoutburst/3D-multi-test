package dev.blackoutburst.game.network.packets

import dev.blackoutburst.game.network.packets.server.*
import java.nio.ByteBuffer

class PacketManager {

    private val packets = mutableMapOf<Int, PacketPlayIn>()

    init {
        packets[0x00] = S00MoveEntity()
        packets[0x01] = S01AddEntity()
        packets[0x02] = S02RemoveEntity()
        packets[0x03] = S03Identification()
        packets[0x04] = S04EntityRotation()
        packets[0x05] = S05SendChunk()
    }

    fun read(data: ByteArray) {
        val buffer = ByteBuffer.wrap(data)
        val id = buffer.get().toInt()

        packets[id]?.decode(buffer)
    }

}