package dev.blackoutburst.server.core.commands

import dev.blackoutburst.server.core.world.World
import dev.blackoutburst.server.network.Client
import dev.blackoutburst.server.network.packets.server.S06Chat

@Command
fun seed(client: Client, args: String) {
    client.write(S06Chat("${World.seed}"))
}