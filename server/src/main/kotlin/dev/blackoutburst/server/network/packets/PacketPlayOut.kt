package dev.blackoutburst.server.network.packets

import java.nio.ByteBuffer

abstract class PacketPlayOut {
    var buffer: ByteBuffer? = null
}