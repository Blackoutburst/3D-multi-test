package dev.blackoutburst.server.network.packets

import java.nio.ByteBuffer

interface PacketPlayIn {
    fun decode(buffer: ByteBuffer): Any
}
