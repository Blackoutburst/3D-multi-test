package dev.blackoutburst.server

import dev.blackoutburst.server.core.world.World
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.WebServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main() {
    World.generate()

    CoroutineScope(Dispatchers.IO).launch { WebServer }

    while(true) {
        Server.addClient()
    }
}