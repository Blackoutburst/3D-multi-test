package dev.blackoutburst.server.core.world

enum class BlockType(val id: Byte, val transparent: Boolean) {
    AIR(0, true),
    GRASS(1, false),
    DIRT(2, false),
    STONE(3, false),
    OAK_LOG(4, false),
    OAK_LEAVES(5, true);

    companion object {
        fun getByID(id: Byte): BlockType {
            return entries.first { it.id == id }
        }
    }
}