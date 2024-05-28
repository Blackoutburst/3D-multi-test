package dev.blackoutburst.game.world

import dev.blackoutburst.game.utils.Textures

enum class BlockType(val id: Byte, val transparent: Boolean, val textures: Array<Int>) {
    AIR(0, true, emptyArray()),
    GRASS(1, false, arrayOf(Textures.GRASS_TOP.ordinal, Textures.GRASS_SIDE.ordinal, Textures.GRASS_SIDE.ordinal, Textures.GRASS_SIDE.ordinal, Textures.GRASS_SIDE.ordinal, Textures.DIRT.ordinal)),
    DIRT(2, false, Array(6) { Textures.DIRT.ordinal }),
    STONE(3, false, Array(6) { Textures.STONE.ordinal }),
    OAK_LOG(4, false, arrayOf(Textures.OAK_LOG_TOP.ordinal, Textures.OAK_SIDE.ordinal, Textures.OAK_SIDE.ordinal, Textures.OAK_SIDE.ordinal, Textures.OAK_SIDE.ordinal, Textures.OAK_LOG_TOP.ordinal)),
    OAK_LEAVES(5, true, Array(6) { Textures.OAK_LEAVES.ordinal });
    companion object {
        fun getByID(id: Byte): BlockType {
            return entries.first { it.id == id }
        }
    }
}