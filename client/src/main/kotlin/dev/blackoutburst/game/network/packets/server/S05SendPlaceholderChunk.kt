package dev.blackoutburst.game.network.packets.server

import dev.blackoutburst.game.Main
import dev.blackoutburst.game.maths.Vector3i
import dev.blackoutburst.game.network.packets.PacketPlayIn
import dev.blackoutburst.game.world.BlockType
import java.nio.ByteBuffer

class S05SendPlaceholderChunk(override val size: Int) : PacketPlayIn() {

    override fun decode(buffer: ByteBuffer) {
        val x = buffer.getInt()
        val y = buffer.getInt()
        val z = buffer.getInt()
        val position = Vector3i(x, y, z)

        Main.glTaskQueue.add {
            Main.world.updateChunk(position, Array(4096) { -1 })
        }
    }
}