package dev.blackoutburst.server.core.entity

import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.packets.server.S01AddEntity
import dev.blackoutburst.server.network.packets.server.S02RemoveEntity

class EntityManager {
    var newId = 0
        private set
        get() {
            return field++
        }

    private val entities = mutableListOf<Entity>()

    fun addEntity(entity: Entity) {
        entities.add(entity)

        Server.write(S01AddEntity(
            entityId = entity.id,
            position = entity.position
        ))
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