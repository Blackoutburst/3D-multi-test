package dev.blackoutburst.server.network.packets

import dev.blackoutburst.server.network.Client
import dev.blackoutburst.server.network.packets.client.C00MoveEntity
import dev.blackoutburst.server.network.packets.client.C01EntityRotation
import dev.blackoutburst.server.network.packets.client.C02UpdateBlock
import java.nio.ByteBuffer

class PacketManager {

    private val packets = mutableMapOf<Int, PacketPlayIn>()

    init {
        packets[0x00] = C00MoveEntity()
        packets[0x01] = C01EntityRotation()
        packets[0x02] = C02UpdateBlock()
    }

    fun read(client: Client, data: ByteArray) {
        val buffer = ByteBuffer.wrap(data)
        val id = buffer.get().toInt()

        packets[id]?.decode(client, buffer)
    }

}