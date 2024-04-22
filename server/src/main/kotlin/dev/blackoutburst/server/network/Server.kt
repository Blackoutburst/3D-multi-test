package dev.blackoutburst.server.network

import dev.blackoutburst.server.network.packets.PacketManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.ServerSocket

object Server {

    private val clients = mutableListOf<Client>()
    private val server = ServerSocket(15000)

    val manager = PacketManager()

    fun read() {
        val socket = server.accept()
        val input = socket.getInputStream()
        val output = socket.getOutputStream()
        val client = Client(socket, input, output)

        clients.add(client)

        CoroutineScope(Dispatchers.IO).launch {
            while (!client.socket.isClosed) {
                client.read()
            }
        }
    }

    fun write(data: ByteArray) {
        for (client in clients) {
            client.write(data)
        }
    }

    fun remove(client: Client) {
        try {
            client.socket.close()
            client.input.close()
            client.output.close()
            clients.remove(client)
        } catch (ignored: Exception) {}
    }
}