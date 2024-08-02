package dev.blackoutburst.server.core.commands

import dev.blackoutburst.server.core.world.World
import dev.blackoutburst.server.network.Client
import dev.blackoutburst.server.network.packets.server.S06Chat

@Command
fun seed(client: Client, args: String) {
    client.write(S06Chat("${World.seed}"))
}

@Command
fun help(client: Client, args: String) {
    client.write(S06Chat("=== Help ==="))
    client.write(S06Chat("/help: Display this message"))
    client.write(S06Chat("/tp: Teleport you to another player or coords"))
    client.write(S06Chat("/seed: Give the server current world seed"))
    client.write(S06Chat("==========="))
}