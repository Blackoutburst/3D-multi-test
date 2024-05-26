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
        entities.forEach {
            if (it.id != Main.connection.id) {
                it.position = it.position.lerp(it.pasSmoothPosition, 0.1f)
                it.rotation = it.rotation.lerp(it.pasSmoothRotation, 0.1f)
            }
            it.update()
        }
    }

    fun render() {
        entities.forEach { it.render() }
    }

    fun setPosition(id: Int, position: Vector3f) {
        if (id == Main.connection.id) return

        entities.find { it.id == id }?.let {
            it.pasSmoothPosition = position
        }
    }

    fun setRotation(id: Int, rotation: Vector2f) {
        if (id == Main.connection.id) return

        entities.find { it.id == id }?.let {
            it.pasSmoothRotation = rotation
        }
    }

    fun removeEntity(id: Int) {
        entities.find { it.id == id }?.let {
            entities.remove(it)
        }
    }
}