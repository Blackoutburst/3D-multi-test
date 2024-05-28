package dev.blackoutburst.game.network.packets.client

import dev.blackoutburst.game.maths.Vector3i
import dev.blackoutburst.game.network.packets.PacketPlayOut
import dev.blackoutburst.game.world.Block
import java.nio.ByteBuffer
import java.nio.ByteOrder

private const val ID: Byte = 0x02

class C02BlockBulkEdit(
    private val blocks: List<Block>
) : PacketPlayOut() {

    init {
        buffer = ByteBuffer.allocate((13 * blocks.size) + 5).clear()

        buffer?.apply {
            order(ByteOrder.BIG_ENDIAN)
            put(ID)
            putInt(blocks.size)
            blocks.forEach {
                put(it.type.id)
                putInt(it.position.x)
                putInt(it.position.y)
                putInt(it.position.z)
            }

        }
    }
}