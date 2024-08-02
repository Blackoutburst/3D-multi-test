package dev.blackoutburst.server.core.commands

import dev.blackoutburst.server.network.Client
import dev.blackoutburst.server.network.packets.server.S06Chat
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.kotlinFunction

object CommandManager {
    private val commands = mutableMapOf<String, KFunction<*>>()

    init {
        try {
            Reflections(
                ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forPackage("dev.blackoutburst.server.core.commands"))
                    .setScanners(Scanners.MethodsAnnotated)
            )
                .getMethodsAnnotatedWith(Command::class.java)
                .forEach{ commands[it.name] = it.kotlinFunction!! }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun execute(client: Client, command: String) {
        try {
            val cmd = command.split(" ")[0].removePrefix("/").trim().lowercase()
            commands[cmd]?.call(client, command.removePrefix("/$cmd".trim()).trim()) ?: run {
                client.write(S06Chat("No such command: [$cmd]"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            client.write(S06Chat(e.message ?: e.cause?.message ?: "Internal error"))
        }
    }
}