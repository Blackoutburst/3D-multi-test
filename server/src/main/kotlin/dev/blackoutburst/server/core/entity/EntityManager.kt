package dev.blackoutburst.server.core.entity

import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.Server.clients
import dev.blackoutburst.server.network.packets.server.S03UpdateEntity
import dev.blackoutburst.server.network.packets.server.S01AddEntity
import dev.blackoutburst.server.network.packets.server.S02RemoveEntity
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.LinkedHashSet

class EntityManager {
    var newId = AtomicInteger(0)

    val entities: MutableList<Entity> = Collections.synchronizedList(LinkedList())

    fun getEntity(id: Int): Entity? {
        val size = entities.size
        for (i in 0 until size) {
            val entity = try { entities[i] } catch (ignored: Exception) { null } ?: continue
            if (entity.id == id) return entity
        }
        return null
    }

    fun addEntity(entity: Entity) {
        entities.add(entity)

        Server.write(S01AddEntity(
            entity.id,
            entity.position,
            entity.rotation
        ))
    }

    fun update(id: Int, position: Vector3f, rotation: Vector2f) {
        getEntity(id)?.let {
            it.position = position
            it.rotation = rotation
        }
    }

    fun sendData() {
        val size = entities.size
        for (i in 0 until size) {
            val entity = try { entities[i] } catch (ignored: Exception) { null } ?: continue

            val clientSize = clients.size
            for (j in 0 until clientSize) {
                val client = try { clients[j] } catch (ignored: Exception) { null } ?: continue
                if (client.entityId == entity.id) continue

                client.write(S03UpdateEntity(entity.id, entity.position, entity.rotation))
            }
        }
    }

    fun removeEntity(id: Int) {
        getEntity(id)?.let {
            entities.remove(it)
        }

        Server.write(S02RemoveEntity(
            entityId = id,
        ))
    }
}