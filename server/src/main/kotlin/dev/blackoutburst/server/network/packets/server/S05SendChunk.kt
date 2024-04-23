package dev.blackoutburst.server.network.packets.server

import dev.blackoutburst.game.maths.Vector3i
import dev.blackoutburst.server.network.packets.PacketPlayOut
import java.nio.ByteOrder

private const val ID: Byte = 0x05

class S05SendChunk(
    private val position: Vector3i,
    private val blockData: List<Byte>
): PacketPlayOut() {

    init {
        buffer.apply {
            order(ByteOrder.BIG_ENDIAN)
            put(ID)
            putInt(position.x)
            putInt(position.y)
            putInt(position.z)
            blockData.forEach {
                put(it)
            }
        }
    }
}