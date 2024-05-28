package dev.blackoutburst.server.core.world

enum class BlockType(val id: Byte) {
    AIR(0),
    GRASS(1),
    DIRT(2),
    STONE(3),
    OAK_LOG(4),
    OAK_LEAVES(5);

    companion object {
        fun getByID(id: Byte): BlockType {
            return entries.first { it.id == id }
        }
    }
}