package dev.blackoutburst.game.network

import dev.blackoutburst.game.network.packets.PacketManager
import dev.blackoutburst.game.network.packets.PacketPlayOut
import dev.blackoutburst.game.utils.io
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

class Connection {

    private var socket: Socket? = null
    private var input: InputStream? = null
    private var output: OutputStream? = null

    private var manager = PacketManager()

    var id = -1

    fun start() {
        try {
            socket = Socket("localhost", 15000).let {
                input = it.getInputStream()
                output = it.getOutputStream()
                it
            }.also {
                io {
                    while (!it.isClosed) {
                        read()
                    }
                }
            }
        } catch (ignored: Exception) {
            close()
        }
    }

    private fun read() {
        try {
            input?.let {
                val idData = it.readNBytes(1)
                if (idData.isEmpty()) {
                    close()
                    return
                }

                val id = manager.getId(idData)
                val size = manager.getSize(id)
                val data = it.readNBytes(size)

                manager.decode(id, data)
            }
        } catch (ignored: Exception) {
            close()
        }
    }

    fun write(packet: PacketPlayOut) {
        try {
            output?.let { out ->
                packet.buffer?.let {
                    io {
                        out.write(it.array())
                        out.flush()
                    }
                }
            }
        } catch (ignored: Exception) {
            close()
        }
    }

    fun close() {
        try {
            input?.close()
            output?.close()
            socket?.close()
        } catch (ignored: Exception) {}
    }
}