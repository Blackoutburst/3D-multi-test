package dev.blackoutburst.server.network

import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

class Client(val socket: Socket, val input: InputStream, val output: OutputStream) {

    fun read() {
        try {
            val data = input.readNBytes(128)
            if (data.isEmpty()) {
                Server.remove(this)
            }

            Server.manager.read(data)
        } catch (ignored: Exception) {
            Server.remove(this)
        }
    }

    fun write(data: ByteArray) {
        try {
            output.write(data)
            output.flush()
        } catch (ignored: Exception) {
            Server.remove(this)
        }
    }
}
