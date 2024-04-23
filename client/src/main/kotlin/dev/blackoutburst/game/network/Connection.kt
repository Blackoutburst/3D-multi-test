package dev.blackoutburst.game.network

import dev.blackoutburst.game.network.packets.PacketManager
import dev.blackoutburst.game.network.packets.PacketPlayOut
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
            socket = Socket("127.0.0.1", 15000).let {
                input = it.getInputStream()
                output = it.getOutputStream()
                it
            }.also {
                CoroutineScope(Dispatchers.IO).launch {
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
                val data = it.readNBytes(5000)
                if (data.isEmpty()) {
                    close()
                }

                manager.read(data)
            }
        } catch (ignored: Exception) {
            close()
        }
    }

    fun write(packet: PacketPlayOut) {
        try {
            output?.let {
                CoroutineScope(Dispatchers.IO).launch {
                    it.write(packet.buffer.array())
                    it.flush()
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