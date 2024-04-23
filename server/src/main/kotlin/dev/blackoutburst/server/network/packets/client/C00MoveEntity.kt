package dev.blackoutburst.server.network.packets.client

import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.server.network.Client
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.packets.PacketPlayIn
import dev.blackoutburst.server.network.packets.server.S00MoveEntity
import java.nio.ByteBuffer

class C00MoveEntity: PacketPlayIn() {

    override fun decode(client: Client, buffer: ByteBuffer) {
        val entityId = buffer.getInt()
        val x = buffer.getFloat()
        val y = buffer.getFloat()
        val z = buffer.getFloat()

        val position = Vector3f(x, y, z)

        if (entityId != client.entityId) return

        Server.entityManger.setPosition(entityId, position)
    }
}