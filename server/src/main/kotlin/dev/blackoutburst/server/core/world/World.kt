package dev.blackoutburst.server.core.world

import dev.blackoutburst.game.maths.Vector3i
import dev.blackoutburst.server.network.Server
import dev.blackoutburst.server.network.packets.server.S05SendChunk
import dev.blackoutburst.server.utils.chunkFloor
import kotlin.random.Random

object World {

    val WORLD_SIZE = 6
    val WORLD_HEIGHT = 2
    val CHUNK_SIZE = 16

    val seed = Random.nextLong()
    val chunks = mutableMapOf<String, Chunk>()

    fun generate() {
        for (x in -WORLD_SIZE until WORLD_SIZE)
            for (y in 0 until WORLD_HEIGHT)
                for (z in -WORLD_SIZE until WORLD_SIZE)
                    addChunk(x * CHUNK_SIZE, y * CHUNK_SIZE, z * CHUNK_SIZE)
    }

    fun updateChunk(position: Vector3i, blockType: Byte) {
        val index = (Vector3i(
            chunkFloor(position.x.toFloat()),
            chunkFloor(position.y.toFloat()),
            chunkFloor(position.z.toFloat())
        ) / CHUNK_SIZE * CHUNK_SIZE)

        val blocks = chunks[index.toString()]?.blocks ?: return

        blocks.find {
            it.position.x == position.x &&
            it.position.y == position.y &&
            it.position.z == position.z
        }?.type = BlockType.getByID(blockType)

        Server.write(S05SendChunk(
            index,
            blocks.map { it.type.id }
        ))
    }

    private fun addChunk(x: Int, y: Int, z: Int) {
        val position = Vector3i(x, y, z)

        chunks[position.toString()] = Chunk(position)
    }
}