package dev.blackoutburst.server

import dev.blackoutburst.server.core.world.World
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.WebServer
import dev.blackoutburst.server.utils.io
import java.util.*
import kotlin.concurrent.schedule

fun main(args: Array<String>) {
    val timer = Timer()

    io { WebServer }

    io {
        timer.schedule(0, 50) {
            Server.entityManger.sendData()
        }
    }

    io {
        timer.schedule(0, 500) {
            println(World.chunks.size)
            World.unloadChunk()
        }
    }

    io {
        while(true) {
            Server.addClient()
        }
    }

    while(true) {
        World.loadChunk(args[0].toInt() * 16)
    }
}