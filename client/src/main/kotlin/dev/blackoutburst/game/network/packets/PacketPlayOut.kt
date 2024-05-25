package dev.blackoutburst.game.network.packets

import java.nio.ByteBuffer

abstract class PacketPlayOut {
    var buffer: ByteBuffer? = null
}
