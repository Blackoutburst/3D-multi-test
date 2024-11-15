package dev.blackoutburst.server.core.world

enum class BlockType(val id: Byte, val transparent: Boolean) {
    ERROR(-1, false),
    AIR(0, true),
    GRASS(1, false),
    DIRT(2, false),
    STONE(3, false),
    OAK_LOG(4, false),
    OAK_LEAVES(5, true),
    GLASS(6, true),
    WATER(7, true),
    SAND(8, false),
    SNOW(9, false);

    companion object {
        fun getByID(id: Byte): BlockType {
            return entries.firstOrNull { it.id == id } ?: ERROR
        }
    }
}