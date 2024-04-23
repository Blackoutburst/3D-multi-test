package dev.blackoutburst.game.network.packets.client

import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.game.maths.Vector3i
import dev.blackoutburst.game.network.packets.PacketPlayOut
import java.nio.ByteOrder

private const val ID: Byte = 0x02

class C02UpdateBlock(
    private val blockType: Byte,
    private val position: Vector3i
) : PacketPlayOut() {

    init {
        buffer.apply {
            order(ByteOrder.BIG_ENDIAN)
            put(ID)
            put(blockType)
            putInt(position.x)
            putInt(position.y)
            putInt(position.z)
        }
    }
}