package dev.blackoutburst.server.network.packets.client

import dev.blackoutburst.server.maths.Vector3i
import dev.blackoutburst.server.core.world.Chunk
import dev.blackoutburst.server.core.world.World
import dev.blackoutburst.server.network.Client
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.packets.PacketPlayIn
import dev.blackoutburst.server.network.packets.server.S04SendChunk
import java.nio.ByteBuffer

class C02BlockBulkEdit(override val size: Int) : PacketPlayIn() {

    override fun decode(client: Client, buffer: ByteBuffer) {
        client.input?.let {
            val updatedChunks = mutableMapOf<String, Chunk>()
            val blockCount = ByteBuffer.wrap(it.readNBytes(4)).getInt()
            val b = ByteBuffer.wrap(it.readNBytes(13 * blockCount))
            for (i in 0 until blockCount) {
                val blockType = b.get()
                val x = b.getInt()
                val y = b.getInt()
                val z = b.getInt()
                val position = Vector3i(x, y, z)
                val chunk = World.updateChunk(position, blockType)
                updatedChunks[chunk.position.toString()] = chunk
            }

            updatedChunks.forEach { (_, v) ->
                Server.write(S04SendChunk(v.position, v.blocks))
            }
        }
    }
}