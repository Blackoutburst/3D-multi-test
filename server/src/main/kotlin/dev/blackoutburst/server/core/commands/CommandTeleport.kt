package dev.blackoutburst.server.core.commands

import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.server.network.Client
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.packets.server.S03UpdateEntityPosition
import dev.blackoutburst.server.network.packets.server.S06Chat

@Command
fun tp(client: Client, args: String) {
    val split = args.split(" ")

    when (split.size) {
        1 -> toNamedEntity(client, split[0])
        2 -> entityToEntity(client, split[0], split[1])
        3 -> toCoords(client, split[0], split[1], split[2])
        else -> client.write(S06Chat("&cInvalid syntax: &e[$args]"))
    }
}

private fun relativeCoords(client: Client, sX: String, sY: String, sZ: String) {
    if (!sX.contains("~")) { client.write(S06Chat("&cInvalid syntax: &e[$sX]")); return }
    if (!sY.contains("~")) { client.write(S06Chat("&cInvalid syntax: &e[$sY]")); return }
    if (!sZ.contains("~")) { client.write(S06Chat("&cInvalid syntax: &e[$sZ]")); return }

    val x = try { sX.replace("~", "").toFloat() } catch (ignored: Exception) { client.write(S06Chat("&cInvalid syntax: &e[$sX]")); return }
    val y = try { sY.replace("~", "").toFloat() } catch (ignored: Exception) { client.write(S06Chat("&cInvalid syntax: &e[$sY]")); return }
    val z = try { sZ.replace("~", "").toFloat() } catch (ignored: Exception) { client.write(S06Chat("&cInvalid syntax: &e[$sZ]")); return }

    Server.entityManger.getEntity(client.entityId)?.let {
        it.position += Vector3f(x, y, z)
        client.write(S03UpdateEntityPosition(client.entityId, it.position, it.rotation))
        client.write(S06Chat("&aTeleporting to &b${it.position.x}&a, &b${it.position.y}&a, &b${it.position.z}&a..."))
    }
}

private fun toCoords(client: Client, sX: String, sY: String, sZ: String) {
    if (sX.contains("~") || sY.contains("~") || sZ.contains("~")) {
        relativeCoords(client, sX, sY, sZ)
        return
    }

    val x = try { sX.toFloat() } catch (ignored: Exception) { client.write(S06Chat("&cInvalid syntax: &e[$sX]")); return }
    val y = try { sY.toFloat() } catch (ignored: Exception) { client.write(S06Chat("&cInvalid syntax: &e[$sY]")); return }
    val z = try { sZ.toFloat() } catch (ignored: Exception) { client.write(S06Chat("&cInvalid syntax: &e[$sZ]")); return }

    Server.entityManger.getEntity(client.entityId)?.let {
        it.position = Vector3f(x, y, z)
        client.write(S03UpdateEntityPosition(client.entityId, it.position, it.rotation))
        client.write(S06Chat("&aTeleporting to &b${it.position.x}&a, &b${it.position.y}&a, &b${it.position.z}&a..."))
    }
}

private fun entityToEntity(client: Client, firstEntityName: String, secondEntityName: String) {
    val firstEntity = Server.entityManger.getEntityByName(firstEntityName)
    if (firstEntity == null) {
        client.write(S06Chat("&eNo such entity $firstEntityName"))
        return
    }

    val secondEntity = Server.entityManger.getEntityByName(secondEntityName)
    if (secondEntity == null) {
        client.write(S06Chat("&eNo such entity $secondEntityName"))
        return
    }

    val clientSize = Server.clients.size
    for (i in 0 until clientSize) {
        val c = try { Server.clients[i] } catch (ignored: Exception) { null } ?: continue
        if (c.entityId != firstEntity.id) continue

        firstEntity.position = secondEntity.position
        firstEntity.rotation = secondEntity.rotation
        c.write(S03UpdateEntityPosition(c.entityId, secondEntity.position, secondEntity.rotation))
        c.write(S06Chat("&aTeleporting to &b$secondEntityName..."))
    }

    if (client.entityId != firstEntity.id) {
        client.write(S06Chat("&aTeleporting &b$firstEntityName &ato &b$secondEntityName..."))
    }
}

private fun toNamedEntity(client: Client, entityName: String) {
    val entity = Server.entityManger.getEntityByName(entityName)
    if (entity == null) {
        client.write(S06Chat("&eNo such entity $entityName"))
        return
    }

    Server.entityManger.getEntity(client.entityId)?.let {
        it.position = entity.position
        it.rotation = entity.rotation
        client.write(S03UpdateEntityPosition(client.entityId, entity.position, entity.rotation))
        client.write(S06Chat("&aTeleporting to &b${entity.name}..."))
    }
}