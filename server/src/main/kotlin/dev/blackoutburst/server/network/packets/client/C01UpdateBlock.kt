package dev.blackoutburst.server.network.packets.client

import dev.blackoutburst.server.maths.Vector3i
import dev.blackoutburst.server.core.world.World
import dev.blackoutburst.server.network.Client
import dev.blackoutburst.server.network.packets.PacketPlayIn
import dev.blackoutburst.server.utils.default
import java.nio.ByteBuffer

class C01UpdateBlock(override val size: Int) : PacketPlayIn() {

    override fun decode(client: Client, buffer: ByteBuffer) {
        val blockType = buffer.get()
        val x = buffer.getInt()
        val y = buffer.getInt()
        val z = buffer.getInt()

        val position = Vector3i(x, y, z)
        default { World.updateChunk(position, blockType) }
    }
}