package dev.blackoutburst.server.network.packets

import dev.blackoutburst.server.network.Client
import dev.blackoutburst.server.network.packets.client.C00UpdateEntity
import dev.blackoutburst.server.network.packets.client.C01UpdateBlock
import java.nio.ByteBuffer

class PacketManager {

    private val packets = mutableMapOf<Int, PacketPlayIn>()

    init {
        packets[0x00] = C00UpdateEntity(24)
        packets[0x01] = C01UpdateBlock(13)
    }

    fun getId(data: ByteArray): Int {
        val buffer = ByteBuffer.wrap(data)

        return buffer.get().toInt()
    }

    fun getSize(id: Int): Int = packets[id]?.size ?: 0

    fun read(id: Int, client: Client, data: ByteArray) {
        val buffer = ByteBuffer.wrap(data)

        packets[id]?.decode(client, buffer)
    }

    fun readWS(client: Client, data: ByteArray) {
        val buffer = ByteBuffer.wrap(data)
        val id = buffer.get().toInt()

        packets[id]?.decode(client, buffer)
    }

}