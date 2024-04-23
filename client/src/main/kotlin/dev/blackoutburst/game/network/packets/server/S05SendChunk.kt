package dev.blackoutburst.game.network.packets.server

import dev.blackoutburst.game.Main
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.game.network.packets.PacketPlayIn
import java.nio.ByteBuffer

class S05SendChunk: PacketPlayIn() {

    override fun decode(buffer: ByteBuffer) {
        val x = buffer.getFloat()
        val y = buffer.getFloat()
        val z = buffer.getFloat()

        val position = Vector3f(x, y, z)
        val blockData = mutableListOf<Byte>()

        for (i in 0 until 4096)
            blockData.add(buffer.get())

        Main.glTaskQueue.add {
            Main.world.placeChunk(position, blockData)
        }
    }
}