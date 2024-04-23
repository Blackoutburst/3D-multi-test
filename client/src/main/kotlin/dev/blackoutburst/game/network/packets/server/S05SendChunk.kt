package dev.blackoutburst.game.network.packets.server

import dev.blackoutburst.game.Main
import dev.blackoutburst.game.maths.Vector3i
import dev.blackoutburst.game.network.packets.PacketPlayIn
import java.nio.ByteBuffer

class S05SendChunk: PacketPlayIn() {

    override fun decode(buffer: ByteBuffer) {
        val x = buffer.getInt()
        val y = buffer.getInt()
        val z = buffer.getInt()

        val position = Vector3i(x, y, z)
        val blockData = mutableListOf<Byte>()

        for (i in 0 until 4096)
            blockData.add(buffer.get())

        Main.glTaskQueue.add {
            Main.world.updateChunk(position, blockData)
        }
    }
}