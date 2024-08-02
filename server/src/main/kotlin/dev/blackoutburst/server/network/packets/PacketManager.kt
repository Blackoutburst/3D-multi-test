package dev.blackoutburst.server.network.packets

import dev.blackoutburst.server.network.Client
import dev.blackoutburst.server.network.packets.client.*
import java.nio.ByteBuffer

class PacketManager {

    private val packets = mutableMapOf<Int, PacketPlayIn>()

    init {
        packets[0x00] = C00UpdateEntity(20)
        packets[0x01] = C01UpdateBlock(13)
        packets[0x02] = C02BlockBulkEdit(0)
        packets[0x03] = C03Chat(4096)
        packets[0x04] = C04ClientMetadata(65)
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