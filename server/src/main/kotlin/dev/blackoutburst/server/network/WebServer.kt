package dev.blackoutburst.server.network

import dev.blackoutburst.server.core.entity.EntityPlayer
import dev.blackoutburst.server.core.world.BlockType
import dev.blackoutburst.server.core.world.World.chunks
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
                    val entity = EntityPlayer(entityManger.newId.getAndIncrement())
                    val client = Client(null, this, null, null, entity.id)

                    client.write(S00Identification(client.entityId))

                    val entitySize = entityManger.entities.size
                    for (i in 0 until entitySize) {
                        val e = try {entityManger.entities[i] } catch (ignored: Exception) { null } ?: continue
                        client.write(S01AddEntity(e.id, e.position, e.rotation))
                    }

                    entityManger.addEntity(entity)
                    clients.add(client)

                    try {
                        for (frame in incoming) {
                            if (frame is Frame.Binary) {
                                client.read(frame)
                            }
                        }
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    } finally {
                        Server.removeClient(client)
                    }
                }
            }
        }.start(wait = true)
    }
}