package dev.blackoutburst.server.network

import dev.blackoutburst.server.core.entity.EntityPlayer
import dev.blackoutburst.server.core.world.BlockType
import dev.blackoutburst.server.core.world.World
import dev.blackoutburst.server.core.world.World.chunks
import dev.blackoutburst.server.maths.Vector3i
import dev.blackoutburst.server.network.Server.clients
import dev.blackoutburst.server.network.Server.entityManger
import dev.blackoutburst.server.network.packets.server.S01AddEntity
import dev.blackoutburst.server.network.packets.server.S00Identification
import dev.blackoutburst.server.network.packets.server.S04SendChunk
import dev.blackoutburst.server.network.packets.server.S05SendMonoTypeChunk
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*

object WebServer {

    init {
        embeddedServer(Netty, port = 16000) {
            install(WebSockets)

            routing {
                webSocket("/game") {
                    val entity = EntityPlayer(Server.entityManger.newId.getAndIncrement())
                    val client = Client(null, this, null, null, entity.id)

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

                    try {
                        for (frame in incoming) {
                            if (frame is Frame.Binary) {
                                client.read(frame)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        Server.removeClient(client)
                    }
                }
            }
        }.start(wait = true)
    }
}