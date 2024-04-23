package dev.blackoutburst.game.network.packets

import java.nio.ByteBuffer

abstract class PacketPlayIn {
    abstract fun decode(buffer: ByteBuffer)
}
