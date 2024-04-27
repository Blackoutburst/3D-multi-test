package dev.blackoutburst.game.network.packets.server

import dev.blackoutburst.game.Main
import dev.blackoutburst.game.core.entity.EntityPlayer
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.game.network.packets.PacketPlayIn
import java.nio.ByteBuffer

class S03Identification: PacketPlayIn() {

    override fun decode(buffer: ByteBuffer) {
        val entityId = buffer.getInt()

        Main.glTaskQueue.add {
            Main.entityManager.addEntity(
                EntityPlayer(
                    id = entityId,
                    position = Vector3f(),
                    world = Main.world,
                    connection = Main.connection,
                )
            )
            Main.connection.id = entityId
        }
    }
}