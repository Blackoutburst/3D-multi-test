package dev.blackoutburst.server.network

import dev.blackoutburst.server.network.packets.PacketPlayOut
import dev.blackoutburst.server.utils.io
import io.ktor.websocket.*
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

class Client(
    val socket: Socket? = null,
    val webSocket: DefaultWebSocketSession? = null,
    val input: InputStream? = null,
    val output: OutputStream? = null,
    val entityId: Int
) {

    fun read(frame: Frame? = null) {
        try {
            frame?.let {
                Server.packetManager.read(this, it.data)
            }

            input?.let {
                val data = it.readNBytes(5000)
                if (data.isEmpty()) {
                    Server.removeClient(this)
                    return
                }
                Server.packetManager.read(this, data)
            }
        } catch (ignored: Exception) {
            Server.removeClient(this)
        }
    }

    fun write(packet: PacketPlayOut) {
        try {
            output?.let {
                it.write(packet.buffer.array())
                it.flush()
            }

            webSocket?.let {
                io { it.send(packet.buffer.array()) }
            }
        } catch (ignored: Exception) {
            Server.removeClient(this)
        }
    }
}
