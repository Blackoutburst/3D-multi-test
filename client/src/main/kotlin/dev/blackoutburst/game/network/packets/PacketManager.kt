package dev.blackoutburst.game.network.packets

import dev.blackoutburst.game.network.packets.server.S00MoveEntity
import dev.blackoutburst.game.network.packets.server.S01AddEntity
import dev.blackoutburst.game.network.packets.server.S02RemoveEntity
import dev.blackoutburst.game.network.packets.server.S03Identification
import java.nio.ByteBuffer

class PacketManager {

    private val packets = mutableMapOf<Int, PacketPlayIn>()

    init {
        packets[0x00] = S00MoveEntity()
        packets[0x01] = S01AddEntity()
        packets[0x02] = S02RemoveEntity()
        packets[0x03] = S03Identification()
    }

    fun read(data: ByteArray) {
        val buffer = ByteBuffer.wrap(data)
        val id = buffer.get().toInt()

        packets[id]?.decode(buffer)
    }

}