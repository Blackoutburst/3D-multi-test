package dev.blackoutburst.server.network.packets.client

import dev.blackoutburst.server.core.commands.CommandManager
import dev.blackoutburst.server.network.Client
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.packets.PacketPlayIn
import dev.blackoutburst.server.network.packets.server.S06Chat
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

class C03Chat(override val size: Int) : PacketPlayIn() {

    override fun decode(client: Client, buffer: ByteBuffer) {
        val data = mutableListOf<Byte>()
        for (i in 0 until 4096) {
            data.add(buffer.get())
        }

        val buff = ByteBuffer.wrap(data.toByteArray())
        val rawString = StandardCharsets.UTF_8.decode(buff).toString().replace("\u0000", "")
        val message = "${client.name}: $rawString"
        Server.chat.add(message)

        if (rawString.startsWith("/"))
            CommandManager.execute(client, rawString)
        else
            Server.write(S06Chat(message))
    }
}