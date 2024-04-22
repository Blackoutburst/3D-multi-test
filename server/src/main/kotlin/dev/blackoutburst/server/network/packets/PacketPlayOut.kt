package dev.blackoutburst.server.network.packets

import java.nio.ByteBuffer

abstract class PacketPlayOut {
    abstract fun encode(vararg data: Any)
}
