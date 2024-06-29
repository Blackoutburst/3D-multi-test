package dev.blackoutburst.server.core.entity

import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.packets.server.S03UpdateEntity
import dev.blackoutburst.server.network.packets.server.S01AddEntity
import dev.blackoutburst.server.network.packets.server.S02RemoveEntity
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.LinkedHashSet

class EntityManager {
    var newId = AtomicInteger(0)

    val entities: MutableSet<Entity> = Collections.synchronizedSet(LinkedHashSet())

    fun addEntity(entity: Entity) {
        synchronized(entities) {
            entities.add(entity)
        }

        Server.write(S01AddEntity(
            entity.id,
            entity.position,
            entity.rotation
        ))
    }

    fun update(id: Int, position: Vector3f, rotation: Vector2f) {
        synchronized(entities) {
            entities.find { it.id == id }?.let {
                it.position = position
                it.rotation = rotation

                Server.write(S03UpdateEntity(it.id, position, rotation))
            }
        }
    }

    fun sendData() {
        synchronized(entities) {
            entities.forEach {
                Server.write(S03UpdateEntity(it.id, it.position, it.rotation))
            }
        }
    }

    fun removeEntity(id: Int) {
        synchronized(entities) {
            entities.find { it.id == id }?.let {
                entities.remove(it)
            }
        }

        Server.write(S02RemoveEntity(
            entityId = id,
        ))
    }
}