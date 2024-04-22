package dev.blackoutburst.server

import dev.blackoutburst.server.network.Server

fun main() {
    while(true) {
        Server.read()
    }
}