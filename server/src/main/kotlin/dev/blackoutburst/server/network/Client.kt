package dev.blackoutburst.server.network

import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.nio.ByteBuffer

class Client(val socket: Socket, val input: InputStream, val output: OutputStream) {

    fun read() {
        try {
            val data = input.readNBytes(12)
            if (data.isEmpty()) {
                Server.remove(this)
            }

            val buffer = ByteBuffer.wrap(data)
            println("X: ${buffer.getFloat()} Y: ${buffer.getFloat()} Z: ${buffer.getFloat()}")
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
