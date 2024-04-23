package dev.blackoutburst.server.network.packets.server

import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.server.network.packets.PacketPlayOut
import java.nio.ByteOrder

private const val ID: Byte = 0x05

class S05SendChunk(
    private val position: Vector3f,
    private val blockData: List<Byte>
): PacketPlayOut() {

    init {
        buffer.apply {
            order(ByteOrder.BIG_ENDIAN)
            put(ID)
            putFloat(position.x)
            putFloat(position.y)
            putFloat(position.z)
            blockData.forEach {
                put(it)
            }
        }
    }
}