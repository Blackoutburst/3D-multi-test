package dev.blackoutburst.game.network.packets.server

import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.game.network.packets.PacketPlayIn
import java.nio.ByteBuffer

class S00MoveEntity: PacketPlayIn() {

    override fun decode(buffer: ByteBuffer) {
        val x = buffer.getFloat()
        val y = buffer.getFloat()
        val z = buffer.getFloat()

        Vector3f(x, y, z)
    }
}