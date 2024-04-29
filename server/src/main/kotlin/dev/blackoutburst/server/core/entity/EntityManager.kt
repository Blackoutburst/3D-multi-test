package dev.blackoutburst.server.core.entity

import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.packets.server.S00MoveEntity
import dev.blackoutburst.server.network.packets.server.S01AddEntity
import dev.blackoutburst.server.network.packets.server.S02RemoveEntity
import dev.blackoutburst.server.network.packets.server.S04EntityRotation
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

    fun setPosition(id: Int, position: Vector3f) {
        entities.find { it.id == id }?.let {
            it.position = position

            Server.write(S00MoveEntity(it.id, position))
        }
    }

    fun setRotation(id: Int, rotation: Vector2f) {
        entities.find { it.id == id }?.let {
            it.rotation = rotation

            Server.write(S04EntityRotation(it.id, rotation))
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