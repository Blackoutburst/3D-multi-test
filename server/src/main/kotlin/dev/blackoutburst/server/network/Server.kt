package dev.blackoutburst.server.network

import dev.blackoutburst.server.core.entity.EntityManager
import dev.blackoutburst.server.core.entity.EntityPlayer
import dev.blackoutburst.server.core.world.BlockType
import dev.blackoutburst.server.core.world.World
import dev.blackoutburst.server.core.world.World.chunks
import dev.blackoutburst.server.maths.Vector3i
import dev.blackoutburst.server.network.packets.PacketManager
import dev.blackoutburst.server.network.packets.PacketPlayOut
import dev.blackoutburst.server.network.packets.server.S01AddEntity
import dev.blackoutburst.server.network.packets.server.S00Identification
import dev.blackoutburst.server.network.packets.server.S04SendChunk
import dev.blackoutburst.server.network.packets.server.S05SendMonoTypeChunk
import dev.blackoutburst.server.utils.io
import io.ktor.websocket.*
import java.net.ServerSocket
import java.util.*
import kotlin.collections.LinkedHashSet

object Server {

    private val server = ServerSocket(15000)

    val clients: MutableSet<Client> = Collections.synchronizedSet(LinkedHashSet())

    val packetManager = PacketManager()
    val entityManger = EntityManager()

    fun addClient() {
        val socket = server.accept()
        val input = socket.getInputStream()
        val output = socket.getOutputStream()
        val entity = EntityPlayer(entityManger.newId.getAndIncrement())
        val client = Client(socket, null, input, output, entity.id)

        client.write(S00Identification(client.entityId))

        synchronized(chunks) {
            chunks
                .map { it.value }
                .filter { it.blocks.any { b -> b != BlockType.AIR.id } }
                .sortedBy { it.position.distance(Vector3i(0, 100, 0)) + if (it.isMonoType()) 100000 else 0 }
                .forEach {
                    if (it.isMonoType()) {
                        client.write(
                            S05SendMonoTypeChunk(
                                position = it.position,
                                type = it.blocks.first()
                            )
                        )
                    } else {
                        client.write(
                            S04SendChunk(
                                position = it.position,
                                blockData = it.blocks
                            )
                        )
                    }
                }
        }

        synchronized(entityManger.entities) {
            entityManger.entities.forEach {
                client.write(S01AddEntity(it.id, it.position, it.rotation))
            }
        }
        entityManger.addEntity(entity)

        synchronized(clients) {
            clients.add(client)
        }

        io {
            client.socket?.let {
                while (!it.isClosed) {
                    client.read()
                }
            }
        }
    }

    fun write(packet: PacketPlayOut) {
        synchronized(clients) {
            clients.forEach {
                it.write(packet)
            }
        }
    }

    fun removeClient(client: Client) {
        try {
            client.socket?.close()
            client.input?.close()
            client.output?.close()
            io { client.webSocket?.close() }
            synchronized(clients) { clients.remove(client) }
            entityManger.removeEntity(client.entityId)
        } catch (ignored: Exception) {}
    }
}