package dev.blackoutburst.server.network

import dev.blackoutburst.server.core.entity.EntityPlayer
import dev.blackoutburst.server.network.packets.PacketPlayOut
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

class Client(
    val socket: Socket,
    val input: InputStream,
    val output: OutputStream,
    val entityId: Int
) {

    fun read() {
        try {
            val data = input.readNBytes(5000)
            if (data.isEmpty()) {
                Server.removeClient(this)
            }

            Server.packetManager.read(this, data)
        } catch (ignored: Exception) {
            Server.removeClient(this)
        }
    }

    fun write(packet: PacketPlayOut) {
        try {
            output.write(packet.buffer.array())
            output.flush()
        } catch (ignored: Exception) {
            Server.removeClient(this)
        }
    }
}
