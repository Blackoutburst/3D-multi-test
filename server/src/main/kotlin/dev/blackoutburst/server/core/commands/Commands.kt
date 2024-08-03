package dev.blackoutburst.server.core.commands

import dev.blackoutburst.server.core.world.World
import dev.blackoutburst.server.network.Client
import dev.blackoutburst.server.network.packets.server.S06Chat

@Command
fun seed(client: Client, args: String) {
    client.write(S06Chat("&e${World.seed}"))
}

@Command
fun help(client: Client, args: String) {
    client.write(S06Chat("&6=== &aHelp &6==="))
    client.write(S06Chat("&e/help&f: &aDisplay this message"))
    client.write(S06Chat("&e/tp&f: &aTeleport you to another player or coords"))
    client.write(S06Chat("&e/seed&f: &aGive the server current world seed"))
    client.write(S06Chat("&6============"))
}