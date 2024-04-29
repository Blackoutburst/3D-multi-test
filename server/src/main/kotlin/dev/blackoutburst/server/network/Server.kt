package dev.blackoutburst.server.network

import dev.blackoutburst.server.core.entity.EntityManager
import dev.blackoutburst.server.core.entity.EntityPlayer
import dev.blackoutburst.server.core.world.BlockType
import dev.blackoutburst.server.core.world.World
import dev.blackoutburst.server.network.packets.PacketManager
import dev.blackoutburst.server.network.packets.PacketPlayOut
import dev.blackoutburst.server.network.packets.server.S01AddEntity
import dev.blackoutburst.server.network.packets.server.S03Identification
import dev.blackoutburst.server.network.packets.server.S05SendChunk
import dev.blackoutburst.server.utils.io
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.ServerSocket

object Server {

    private val server = ServerSocket(15000)

    val clients = mutableListOf<Client>()

    val packetManager = PacketManager()
    val entityManger = EntityManager()

    fun addClient() {
        val socket = server.accept()
        val input = socket.getInputStream()
        val output = socket.getOutputStream()
        val entity = EntityPlayer(entityManger.newId)
        val client = Client(socket, null, input, output, entity.id)

        client.write(S03Identification(client.entityId))

        World.chunks.filter {
            it.value.blocks.any { b -> b.type != BlockType.AIR }
        }.forEach {
            client.write(S05SendChunk(
                position = it.value.position,
                blockData = it.value.blocks.map { b -> b.type.id }
            ))
        }

        entityManger.entities.forEach {
            client.write(S01AddEntity(it.id, it.position, it.rotation))
        }
        client.write(S03Identification(client.entityId))
        entityManger.addEntity(entity)
        clients.add(client)

        io {
            client.socket?.let {
                while (!it.isClosed) {
                    client.read()
                }
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
            client.socket?.close()
            client.input?.close()
            client.output?.close()
            io { client.webSocket?.close() }
            clients.remove(client)
            entityManger.removeEntity(client.entityId)
        } catch (ignored: Exception) {}
    }
}