package dev.blackoutburst.server.core.entity

import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.packets.server.S01AddEntity
import dev.blackoutburst.server.network.packets.server.S02RemoveEntity

class EntityManager {
    var newId = 0
        private set
        get() {
            return field++
        }

    val entities = mutableListOf<Entity>()

    fun addEntity(entity: Entity) {
        entities.add(entity)

        Server.write(S01AddEntity(
            entity.id,
            entity.position,
            entity.rotation
        ))
    }

    fun setPosition(id: Int, position: Vector3f) {
        entities.find { it.id == id }?.let {
            it.position = position
        }
    }

    fun setRotation(id: Int, rotation: Vector2f) {
        entities.find { it.id == id }?.let {
            it.rotation = rotation
        }
    }

    fun removeEntity(entity: Entity) {
        entities.find { it.id == entity.id }?.let {
            entities.remove(it)
        }

        Server.write(S02RemoveEntity(
            entityId = entity.id,
        ))
    }
}