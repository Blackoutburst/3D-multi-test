package dev.blackoutburst.server.core.entity

import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.packets.server.S03UpdateEntity
import dev.blackoutburst.server.network.packets.server.S01AddEntity
import dev.blackoutburst.server.network.packets.server.S02RemoveEntity
import java.util.concurrent.atomic.AtomicInteger

class EntityManager {
    var newId = AtomicInteger(0)

    val entities = mutableListOf<Entity>()

    fun addEntity(entity: Entity) {
        entities.add(entity)

        Server.write(S01AddEntity(
            entity.id,
            entity.position,
            entity.rotation
        ))
    }

    fun update(id: Int, position: Vector3f, rotation: Vector2f) {
        entities.find { it.id == id }?.let {
            it.position = position
            it.rotation = rotation

            Server.write(S03UpdateEntity(it.id, position, rotation))
        }
    }

    fun removeEntity(id: Int) {
        entities.find { it.id == id }?.let {
            entities.remove(it)
        }

        Server.write(S02RemoveEntity(
            entityId = id,
        ))
    }
}