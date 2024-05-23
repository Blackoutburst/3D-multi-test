package dev.blackoutburst.server.core.world

enum class BlockType(val id: Byte) {
    AIR(0),
    GRASS(1),
    DIRT(2);

    companion object {
        fun getByID(id: Byte): BlockType {
            return entries.first { it.id == id }
        }
    }
}