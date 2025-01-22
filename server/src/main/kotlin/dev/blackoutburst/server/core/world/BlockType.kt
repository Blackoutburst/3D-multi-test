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
    SNOW(9, false),
    OAK_PLANKS(10, false),
    STONE_BRICKS(11, false),
    NETHERRACK(12, false),
    GOLD_BLOCKS(13, false),
    PACKED_ICE(14, false),
    LAVA(15, false),
    BARREL(16, false),
    BOOKSHELF(17, false);

    companion object {
        fun getByID(id: Byte): BlockType {
            return entries.firstOrNull { it.id == id } ?: ERROR
        }
    }
}