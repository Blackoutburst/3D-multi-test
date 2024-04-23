package dev.blackoutburst.server.network

import dev.blackoutburst.server.core.entity.EntityManager
import dev.blackoutburst.server.core.entity.EntityPlayer
import dev.blackoutburst.server.network.packets.PacketManager
import dev.blackoutburst.server.network.packets.PacketPlayOut
import dev.blackoutburst.server.network.packets.server.S01AddEntity
import dev.blackoutburst.server.network.packets.server.S03Identification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.ServerSocket

object Server {

    private val clients = mutableListOf<Client>()
    private val server = ServerSocket(15000)

    val packetManager = PacketManager()
    val entityManger = EntityManager()

    fun addClient() {
        val socket = server.accept()
        val input = socket.getInputStream()
        val output = socket.getOutputStream()
        val entity = EntityPlayer(entityManger.newId)
        val client = Client(socket, input, output, entity)

        client.write(S03Identification(client.entity.id))
        entityManger.entities.forEach {
            client.write(S01AddEntity(it.id, it.position, it.rotation))
        }
        client.write(S03Identification(client.entity.id))
        entityManger.addEntity(client.entity)
        clients.add(client)

        CoroutineScope(Dispatchers.IO).launch {
            while (!client.socket.isClosed) {
                client.read()
            }
        }
    }

    fun write(packet: PacketPlayOut) {
        for (client in clients) {
            client.write(packet)
        }
    }

    fun removeClient(client: Client) {
        try {
            client.socket.close()
            client.input.close()
            client.output.close()
            clients.remove(client)
            entityManger.removeEntity(client.entity)
        } catch (ignored: Exception) {}
    }
}