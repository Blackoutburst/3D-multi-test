package dev.blackoutburst.server.network.packets.client

import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.packets.PacketPlayIn
import dev.blackoutburst.server.network.packets.server.S04EntityRotation
import java.nio.ByteBuffer

class C01EntityRotation: PacketPlayIn() {

    override fun decode(buffer: ByteBuffer) {
        val entityId = buffer.getInt()
        val x = buffer.getFloat()
        val y = buffer.getFloat()

        val rotation = Vector2f(x, y)

        Server.entityManger.setRotation(entityId, rotation)
        Server.write(S04EntityRotation(entityId, rotation))
    }
}