package dev.blackoutburst.server.network.packets.client

import dev.blackoutburst.server.network.Client
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.packets.PacketPlayIn
import dev.blackoutburst.server.network.packets.server.S06Chat
import dev.blackoutburst.server.network.packets.server.S07UpdateEntityMetadata
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

class C04ClientMetadata(override val size: Int) : PacketPlayIn() {

    override fun decode(client: Client, buffer: ByteBuffer) {
        val renderDistance = buffer.get()
        val data = mutableListOf<Byte>()
        for (i in 0 until 64) {
            data.add(buffer.get())
        }

        val buff = ByteBuffer.wrap(data.toByteArray())
        val name = StandardCharsets.UTF_8.decode(buff).toString()

        client.renderDistance = renderDistance.toInt()
        client.name = name

        Server.entityManger.getEntity(client.entityId)?.let {
            it.name = name
            Server.write(S07UpdateEntityMetadata(it.id, it.name))
        }
    }
}