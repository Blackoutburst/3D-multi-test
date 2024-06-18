package dev.blackoutburst.server

import dev.blackoutburst.server.core.world.World
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.WebServer
import dev.blackoutburst.server.utils.Time
import dev.blackoutburst.server.utils.io

fun main(args: Array<String>) {
    World.generate(args[0].toInt(), args[1].toInt())

    io { WebServer }

    io {
        while(true) {
            if (Time.doUpdate()) {
                Server.entityManger.sendData()
            }
        }
    }

    while(true) {
        Server.addClient()
    }
}