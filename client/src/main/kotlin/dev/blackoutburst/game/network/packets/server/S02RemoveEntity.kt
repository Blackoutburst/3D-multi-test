package dev.blackoutburst.game.network.packets.server

import dev.blackoutburst.game.Main
import dev.blackoutburst.game.network.packets.PacketPlayIn
import java.nio.ByteBuffer

class S02RemoveEntity: PacketPlayIn() {

    override fun decode(buffer: ByteBuffer) {
        val entityId = buffer.getInt()

        Main.entityManager.removeEntity(entityId)
    }
}