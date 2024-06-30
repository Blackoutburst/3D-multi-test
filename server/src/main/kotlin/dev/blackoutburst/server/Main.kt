package dev.blackoutburst.server

import dev.blackoutburst.server.core.world.World
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.WebServer
import dev.blackoutburst.server.utils.io
import java.io.File
import java.util.*
import kotlin.concurrent.schedule

fun main(args: Array<String>) {
    val timer = Timer()

    File("./world").mkdir()

    //World.generate(args[0].toInt(), args[1].toInt())

    io { WebServer }

    io {
        timer.schedule(0, 50) {
            Server.entityManger.sendData()
        }
    }

    timer.schedule(0, 1000) {
        println("Chunk count = ${World.chunks.size}")
    }

    io {
        while(true) {
            Server.addClient()
        }
    }

    while(true) {
        World.update()
    }
}