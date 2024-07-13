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

    val clients: MutableList<Client> = Collections.synchronizedList(LinkedList())
    val packetManager = PacketManager()
    val entityManger = EntityManager()

    fun addClient() {
        val socket = server.accept()
        val input = socket.getInputStream()
        val output = socket.getOutputStream()
        val entity = EntityPlayer(entityManger.newId.getAndIncrement())
        val client = Client(socket, null, input, output, entity.id)

        client.write(S00Identification(client.entityId))

        val entitySize = entityManger.entities.size
        for (i in 0 until entitySize) {
            val e = try {entityManger.entities[i] } catch (ignored: Exception) { null } ?: continue
            client.write(S01AddEntity(e.id, e.position, e.rotation))
        }

        val chunkSize = chunks.size
        for (i in 0 until chunkSize) {
            val chunk = try { chunks.values.toList()[i] } catch (ignored: Exception) { null } ?: continue
            if (chunk.isMonoType()) {
                if (chunk.blocks.first() == BlockType.AIR.id) continue
                client.write(S05SendMonoTypeChunk(chunk.position, chunk.blocks.first()))
            } else {
                client.write(S04SendChunk(chunk.position, chunk.blocks))
            }
        }
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
        clients.forEach {
            it.write(packet)
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