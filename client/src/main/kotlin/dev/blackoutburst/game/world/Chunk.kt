package dev.blackoutburst.game.world

import dev.blackoutburst.game.maths.Vector3i
import dev.blackoutburst.game.utils.concatenateFloatArray
import dev.blackoutburst.game.utils.concatenateIntArray
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.*
import org.lwjgl.opengl.GL30.*
import java.nio.Buffer

private fun getVertices(position: Vector3i, textures: Array<Int>): FloatArray = floatArrayOf(
    //TOP
    0.0f + position.x, 1.0f + position.y, 0.0f + position.z, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, textures[0].toFloat(),
    1.0f + position.x, 1.0f + position.y, 0.0f + position.z, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, textures[0].toFloat(),
    1.0f + position.x, 1.0f + position.y, 1.0f + position.z, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, textures[0].toFloat(),
    0.0f + position.x, 1.0f + position.y, 1.0f + position.z, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, textures[0].toFloat(),

    //FRONT
    0.0f + position.x, 0.0f + position.y, 0.0f + position.z, 1.0f, 1.0f, 0.0f, 0.0f, -1.0f, textures[1].toFloat(),
    1.0f + position.x, 0.0f + position.y, 0.0f + position.z, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f, textures[1].toFloat(),
    1.0f + position.x, 1.0f + position.y, 0.0f + position.z, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, textures[1].toFloat(),
    0.0f + position.x, 1.0f + position.y, 0.0f + position.z, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f, textures[1].toFloat(),

    //BACK
    0.0f + position.x, 0.0f + position.y, 1.0f + position.z, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, textures[2].toFloat(),
    1.0f + position.x, 0.0f + position.y, 1.0f + position.z, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, textures[2].toFloat(),
    1.0f + position.x, 1.0f + position.y, 1.0f + position.z, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, textures[2].toFloat(),
    0.0f + position.x, 1.0f + position.y, 1.0f + position.z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, textures[2].toFloat(),

    //LEFT
    0.0f + position.x, 0.0f + position.y, 0.0f + position.z, 1.0f, 1.0f, -1.0f, 0.0f, 0.0f, textures[3].toFloat(),
    0.0f + position.x, 1.0f + position.y, 0.0f + position.z, 1.0f, 0.0f, -1.0f, 0.0f, 0.0f, textures[3].toFloat(),
    0.0f + position.x, 1.0f + position.y, 1.0f + position.z, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, textures[3].toFloat(),
    0.0f + position.x, 0.0f + position.y, 1.0f + position.z, 0.0f, 1.0f, -1.0f, 0.0f, 0.0f, textures[3].toFloat(),

    //RIGHT
    1.0f + position.x, 0.0f + position.y, 0.0f + position.z, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, textures[4].toFloat(),
    1.0f + position.x, 1.0f + position.y, 0.0f + position.z, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, textures[4].toFloat(),
    1.0f + position.x, 1.0f + position.y, 1.0f + position.z, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, textures[4].toFloat(),
    1.0f + position.x, 0.0f + position.y, 1.0f + position.z, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, textures[4].toFloat(),

    //BOTTOM
    0.0f + position.x, 0.0f + position.y, 0.0f + position.z, 0.0f, 1.0f, 0.0f, -1.0f, 0.0f, textures[5].toFloat(),
    1.0f + position.x, 0.0f + position.y, 0.0f + position.z, 1.0f, 1.0f, 0.0f, -1.0f, 0.0f, textures[5].toFloat(),
    1.0f + position.x, 0.0f + position.y, 1.0f + position.z, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, textures[5].toFloat(),
    0.0f + position.x, 0.0f + position.y, 1.0f + position.z, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, textures[5].toFloat(),
)

private fun getIndices(offset: Int): IntArray = intArrayOf(
    // TOP
    0 + offset, 2 + offset, 1 + offset, 0 + offset, 3 + offset, 2 + offset,
    // FRONT
    4 + offset, 6 + offset, 5 + offset, 4 + offset, 7 + offset, 6 + offset,
    // BACK
    9 + offset, 10 + offset, 8 + offset, 10 + offset, 11 + offset, 8 + offset,
    // LEFT
    12 + offset, 14 + offset, 13 + offset, 12 + offset, 15 + offset, 14 + offset,
    // RIGHT
    17 + offset, 18 + offset, 16 + offset, 18 + offset, 19 + offset, 16 + offset,
    // BOTTOM
    21 + offset, 22 + offset, 20 + offset, 22 + offset, 23 + offset, 20 + offset
)

class Chunk(
    val position: Vector3i,
    val blocks: Array<Byte> = Array(4096) { BlockType.AIR.id }
) {
    private var vaoID = 0
    private var vboID = 0
    private var eboID = 0
    var vertexCount = 0
    var indexCount = 0

    companion object {

        fun getIndex(position: Vector3i, chunkSize: Int): Vector3i {
            return Vector3i(
                (if (position.x < 0) (position.x + 1) / chunkSize - 1 else position.x / chunkSize) * chunkSize,
                (if (position.y < 0) (position.y + 1) / chunkSize - 1 else position.y / chunkSize) * chunkSize,
                (if (position.z < 0) (position.z + 1) / chunkSize - 1 else position.z / chunkSize) * chunkSize
            )
        }
    }

    fun blockAt(x: Int, y: Int, z: Int): Byte = blocks[xyzToIndex(x, y, z)]

    fun xyzToIndex(x: Int, y: Int, z: Int): Int = x + 16 * (y + 16 * z)

    fun isAir(x: Int, y: Int, z: Int): Boolean {
        return if (isWithinBounds(x, y, z)) {
            val type = BlockType.getByID(blockAt(x, y, z))

            type == BlockType.AIR || type.transparent
        } else true
    }

    fun isWithinBounds(x: Int, y: Int, z: Int): Boolean = x in 0 until 16 && y in 0 until 16 && z in 0 until 16

    fun indexToXYZ(index: Int): Vector3i = Vector3i(index % 16, (index / 16) % 16, (index / (16 * 16)) % 16) + this.position

    fun isVisible(block: Block, chunk: Chunk): Boolean {
        val pos = block.position - chunk.position
        val x = pos.x
        val y = pos.y
        val z = pos.z

        return chunk.isAir(x + 1, y, z) || chunk.isAir(x - 1, y, z) ||
                chunk.isAir(x, y + 1, z) || chunk.isAir(x, y - 1, z) ||
                chunk.isAir(x, y, z + 1) || chunk.isAir(x, y, z - 1)
    }

    fun update() {
        val filteredBlocks = blocks
            .mapIndexed { index, value ->
                Block(BlockType.getByID(value), indexToXYZ(index))
            }.filter {
                wb -> wb.type != BlockType.AIR && isVisible(wb, this)
            }

        val vertices = concatenateFloatArray(filteredBlocks.map { getVertices(it.position, it.type.textures) })
        val indices = concatenateIntArray(List(filteredBlocks.size) { i -> getIndices(i * 24) })

        vertexCount = vertices.size / 9

        glDeleteVertexArrays(vaoID)
        glDeleteBuffers(vboID)
        glDeleteBuffers(eboID)

        vaoID = glGenVertexArrays()
        vboID = glGenBuffers()
        eboID = glGenBuffers()

        glBindVertexArray(vaoID)

        // VBO
        val vertexBuffer = BufferUtils.createFloatBuffer(vertices.size)
        (vertexBuffer.put(vertices) as Buffer).flip()

        glBindBuffer(GL_ARRAY_BUFFER, vboID)
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW)

        // EBO
        val indexBuffer = BufferUtils.createIntBuffer(indices.size)
        (indexBuffer.put(indices) as Buffer).flip()

        indexCount = indices.size

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW)

        // Pos
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 36, 0)
        glEnableVertexAttribArray(0)

        // UV
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 36, 12)
        glEnableVertexAttribArray(1)

        // Norm
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 36, 20)
        glEnableVertexAttribArray(2)

        // Tex
        glVertexAttribPointer(3, 1, GL_FLOAT, false, 36, 32)
        glEnableVertexAttribArray(3)

        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
    }


    fun render() {
        glBindVertexArray(vaoID)
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0)
    }
}