package dev.blackoutburst.server.network.packets

import dev.blackoutburst.server.network.Client
import java.nio.ByteBuffer

abstract class PacketPlayIn {
    abstract fun decode(client: Client, buffer: ByteBuffer)
}