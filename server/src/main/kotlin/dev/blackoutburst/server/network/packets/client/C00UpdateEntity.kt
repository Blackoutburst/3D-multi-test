package dev.blackoutburst.server.network.packets.client

import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.server.network.Client
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.packets.PacketPlayIn
import java.nio.ByteBuffer

class C00UpdateEntity(override val size: Int) : PacketPlayIn() {

    override fun decode(client: Client, buffer: ByteBuffer) {
        val entityId = buffer.getInt()
        val x = buffer.getFloat()
        val y = buffer.getFloat()
        val z = buffer.getFloat()
        val yaw = buffer.getFloat()
        val pitch = buffer.getFloat()


        val position = Vector3f(x, y, z)
        val rotation = Vector2f(yaw, pitch)

        if (entityId != client.entityId) return

        Server.entityManger.update(entityId, position, rotation)
    }
}