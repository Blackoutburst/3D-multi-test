package dev.blackoutburst.game.network.packets.client

import dev.blackoutburst.game.maths.Vector3i
import dev.blackoutburst.game.network.packets.PacketPlayOut
import java.nio.ByteBuffer
import java.nio.ByteOrder

private const val ID: Byte = 0x01

class C01UpdateBlock(
    private val blockType: Byte,
    private val position: Vector3i
) : PacketPlayOut() {

    init {
        buffer = ByteBuffer.allocate(14).clear()

        buffer?.apply {
            order(ByteOrder.BIG_ENDIAN)
            put(ID)
            put(blockType)
            putInt(position.x)
            putInt(position.y)
            putInt(position.z)
        }
    }
}