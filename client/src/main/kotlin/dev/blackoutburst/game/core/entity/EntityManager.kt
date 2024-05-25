package dev.blackoutburst.game.core.entity

import dev.blackoutburst.game.Main
import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.game.maths.Vector3f
import java.util.*
import kotlin.collections.LinkedHashSet

class EntityManager {
    private val entities: MutableSet<Entity> = Collections.synchronizedSet(LinkedHashSet())

    fun addEntity(entity: Entity) {
        if (entity.id == Main.connection.id) return

        entities.add(entity)
    }

    fun update() {
        entities.forEach { it.update() }
    }

    fun render() {
        entities.forEach { it.render() }
    }

    fun setPosition(id: Int, position: Vector3f) {
        if (id == Main.connection.id) return

        entities.find { it.id == id }?.let {
            it.position = position
        }
    }

    fun setRotation(id: Int, rotation: Vector2f) {
        if (id == Main.connection.id) return

        entities.find { it.id == id }?.let {
            it.rotation = rotation
        }
    }

    fun removeEntity(id: Int) {
        entities.find { it.id == id }?.let {
            entities.remove(it)
        }
    }
}