package dev.blackoutburst.game.network.packets.server

import dev.blackoutburst.game.Main
import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.game.network.packets.PacketPlayIn
import java.nio.ByteBuffer

class S04EntityRotation: PacketPlayIn() {

    override fun decode(buffer: ByteBuffer) {
        val entityId = buffer.getInt()
        val x = buffer.getFloat()
        val y = buffer.getFloat()

        Main.entityManager.setRotation(entityId, Vector2f(x, y))
    }
}