package dev.blackoutburst.server

import dev.blackoutburst.server.core.world.World
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.WebServer
import dev.blackoutburst.server.utils.io
import java.util.*
import kotlin.concurrent.schedule
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.isEmpty()) error("You must specifie the server render distance")

    val timer = Timer()

    io { WebServer }

    io {
        timer.schedule(0, 50) {
            Server.entityManger.sendData()
        }
    }

    io {
        timer.schedule(0, 500) {
            World.unloadChunk()
        }
    }

    io {
        while(true) {
            Server.addClient()
        }
    }

    while(true) {
        World.loadChunk(args[0].toInt())
    }
}