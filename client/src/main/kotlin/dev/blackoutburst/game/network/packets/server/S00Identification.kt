package dev.blackoutburst.game.network.packets.server

import dev.blackoutburst.game.Main
import dev.blackoutburst.game.core.entity.EntityPlayer
import dev.blackoutburst.game.main
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.game.network.packets.PacketPlayIn
import dev.blackoutburst.game.utils.main
import dev.blackoutburst.game.utils.unconfined
import java.nio.ByteBuffer

class S00Identification(override val size: Int): PacketPlayIn() {

    override fun decode(buffer: ByteBuffer) {
        val entityId = buffer.getInt()

        main {
            Main.entityManager.addEntity(
                EntityPlayer(
                    id = entityId,
                    position = Vector3f(0f, 50f, 0f),
                    world = Main.world,
                    connection = Main.connection,
                )
            )
            Main.connection.id = entityId
        }
    }
}