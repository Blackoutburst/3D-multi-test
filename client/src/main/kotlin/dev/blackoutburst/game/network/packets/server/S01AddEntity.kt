package dev.blackoutburst.game.network.packets.server

import dev.blackoutburst.game.Main
import dev.blackoutburst.game.core.entity.EntityOtherPlayer
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.game.network.packets.PacketPlayIn
import java.nio.ByteBuffer

class S01AddEntity: PacketPlayIn() {

    override fun decode(buffer: ByteBuffer) {
        val entityId = buffer.getInt()
        val x = buffer.getFloat()
        val y = buffer.getFloat()
        val z = buffer.getFloat()

        Vector3f(x, y, z)

        Main.glTaskQueue.add {
            Main.entityManager.addEntity(EntityOtherPlayer(entityId, Vector3f(x, y, z)))
        }
    }
}