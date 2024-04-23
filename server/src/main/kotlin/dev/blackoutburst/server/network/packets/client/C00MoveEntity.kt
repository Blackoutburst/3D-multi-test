package dev.blackoutburst.server.network.packets.client

import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.server.network.packets.PacketPlayIn
import java.nio.ByteBuffer

class C00MoveEntity: PacketPlayIn() {

    override fun decode(buffer: ByteBuffer) {
        val entityId = buffer.getInt()
        val x = buffer.getFloat()
        val y = buffer.getFloat()
        val z = buffer.getFloat()
    }
}