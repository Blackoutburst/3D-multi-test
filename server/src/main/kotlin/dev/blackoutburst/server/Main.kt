package dev.blackoutburst.server

import dev.blackoutburst.server.core.world.World
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.WebServer

fun main() {
    World.generate()

    while(true) {
        WebServer
        Server.addClient()
    }
}