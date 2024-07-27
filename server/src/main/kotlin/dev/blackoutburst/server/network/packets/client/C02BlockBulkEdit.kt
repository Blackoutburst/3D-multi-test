package dev.blackoutburst.server.network.packets.client

import dev.blackoutburst.server.maths.Vector3i
import dev.blackoutburst.server.core.world.Chunk
import dev.blackoutburst.server.core.world.World
import dev.blackoutburst.server.network.Client
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.packets.PacketPlayIn
import dev.blackoutburst.server.network.packets.server.S04SendChunk
import dev.blackoutburst.server.network.packets.server.S05SendMonoTypeChunk
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.nio.ByteBuffer

class C02BlockBulkEdit(override val size: Int) : PacketPlayIn() {

    override fun decode(client: Client, buffer: ByteBuffer): Unit = runBlocking {
        client.input?.let {
            val updatedChunks = mutableMapOf<String, Chunk>()
            val blockCount = ByteBuffer.wrap(it.readNBytes(4)).getInt()
            val b = ByteBuffer.wrap(it.readNBytes(13 * blockCount))
            coroutineScope {
                for (i in 0 until blockCount) {
                    launch {
                        val blockType = b.get()
                        val x = b.getInt()
                        val y = b.getInt()
                        val z = b.getInt()
                        val position = Vector3i(x, y, z)
                        val chunk = World.updateChunk(position, blockType, false)
                        updatedChunks[chunk.position.toString()] = chunk
                    }
                }
            }

            updatedChunks.forEach { (_, v) ->
                val playerSize = v.players.size
                for (i in 0 until playerSize) {
                    val player = Server.getClientByEntityId(v.players[i]) ?: continue

                    if (v.isMonoType())
                        player.write(S05SendMonoTypeChunk(v.position, v.blocks.first()))
                    else
                        player.write(S04SendChunk(v.position, v.blocks))
                }
            }
        }
    }
}