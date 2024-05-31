package dev.blackoutburst.game.network.packets

import dev.blackoutburst.game.network.packets.server.*
import java.nio.ByteBuffer

class PacketManager {

    private val packets = mutableMapOf<Int, PacketPlayIn>()

    init {
        packets[0x00] = S00Identification(4)
        packets[0x01] = S01AddEntity(24)
        packets[0x02] = S02RemoveEntity(4)
        packets[0x03] = S03UpdateEntity(24)
        packets[0x04] = S04SendChunk(4108)
        packets[0x05] = S05SendMonoTypeChunk(13)
    }

    fun getId(data: ByteArray): Int {
        val buffer = ByteBuffer.wrap(data)

        return buffer.get().toInt()
    }

    fun getSize(id: Int): Int = packets[id]?.size ?: 0

    fun decode(id: Int, data: ByteArray) {
        val buffer = ByteBuffer.wrap(data)

        packets[id]?.decode(buffer)
    }

}