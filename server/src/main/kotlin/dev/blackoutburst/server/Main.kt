package dev.blackoutburst.server

import dev.blackoutburst.server.core.world.World
import dev.blackoutburst.server.network.Server

fun main() {
    World.generate()

    while(true) {
        Server.addClient()
    }
}