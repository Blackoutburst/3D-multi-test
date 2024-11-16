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
    val entityId: Int,
    var renderDistance: Int = 2,
    var name: String = "Jeff$entityId",
) {

    fun read(frame: Frame? = null) {
        try {
            frame?.let {
                Server.packetManager.readWS(this, it.data)
            }

            input?.let {
                val idData = it.readNBytes(1)
                if (idData.isEmpty()) {
                    Server.removeClient(this)
                    return
                }

                val id = Server.packetManager.getId(idData)
                val size = Server.packetManager.getSize(id)
                val data = it.readNBytes(size)

                Server.packetManager.read(id, this, data)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            Server.removeClient(this)
        }
    }

    fun write(packet: PacketPlayOut) {
            output?.let { out ->
                io {
                    try {
                        packet.buffer?.let {
                            out.write(it.array())
                            out.flush()
                        }
                    } catch (e: Throwable) {
                        e.printStackTrace()
                        Server.removeClient(this@Client)
                    }
                }
            }

            webSocket?.let { ws ->
                io {
                    try {
                        packet.buffer?.let {
                            ws.send(it.array())
                        }
                    } catch (e: Throwable) {
                        e.printStackTrace()
                        Server.removeClient(this@Client)
                    }
                }
            }
    }
}
