package dev.blackoutburst.game.world

import dev.blackoutburst.game.utils.Textures

enum class BlockType(val id: Byte, val textures: Array<Int>) {
    AIR(0, emptyArray()),
    GRASS(1, arrayOf(Textures.GRASS_TOP.ordinal, Textures.GRASS_SIDE.ordinal, Textures.GRASS_SIDE.ordinal, Textures.GRASS_SIDE.ordinal, Textures.GRASS_SIDE.ordinal, Textures.DIRT.ordinal)),
    DIRT(2, Array(6) { Textures.DIRT.ordinal }),
    STONE(3, Array(6) { Textures.STONE.ordinal });

    companion object {
        fun getByID(id: Byte): BlockType {
            return entries.first { it.id == id }
        }
    }
}