package dev.blackoutburst.game.network.packets

import java.nio.ByteBuffer

abstract class PacketPlayIn {
    abstract val size: Int
    abstract fun decode(buffer: ByteBuffer)
}
