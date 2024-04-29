package dev.blackoutburst.server.network

import dev.blackoutburst.server.core.entity.EntityPlayer
import dev.blackoutburst.server.core.world.BlockType
import dev.blackoutburst.server.core.world.World
import dev.blackoutburst.server.network.packets.server.S01AddEntity
import dev.blackoutburst.server.network.packets.server.S03Identification
import dev.blackoutburst.server.network.packets.server.S05SendChunk
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
                    val entity = EntityPlayer(Server.entityManger.newId)
                    val client = Client(null, this, null, null, entity.id)

                    client.write(S03Identification(client.entityId))

                    World.chunks.filter {
                        it.value.blocks.any { b -> b.type != BlockType.AIR }
                    }.forEach {
                        client.write(S05SendChunk(
                            position = it.value.position,
                            blockData = it.value.blocks.map { b -> b.type.id }
                        ))
                    }

                    Server.entityManger.entities.forEach {
                        client.write(S01AddEntity(it.id, it.position, it.rotation))
                    }

                    client.write(S03Identification(client.entityId))
                    Server.entityManger.addEntity(entity)
                    Server.clients.add(client)

                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            println("inc, ${frame.readText()}")
                            send(Frame.Text(frame.readText()))
                        }
                    }
                }
            }
        }.start(wait = true)
    }
}