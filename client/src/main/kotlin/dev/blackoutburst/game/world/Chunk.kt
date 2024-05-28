package dev.blackoutburst.game.world

import dev.blackoutburst.game.maths.Vector3i
import dev.blackoutburst.game.utils.concatenateFloatArray
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.glBindVertexArray
import java.nio.Buffer

fun getVertices(position: Vector3i, textures: Array<Int>): FloatArray = floatArrayOf(
    //TOP
    0.0f + position.x, 1.0f + position.y, 0.0f + position.z, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, textures[0].toFloat(),
    1.0f + position.x, 1.0f + position.y, 1.0f + position.z, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, textures[0].toFloat(),
    1.0f + position.x, 1.0f + position.y, 0.0f + position.z, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, textures[0].toFloat(),
    1.0f + position.x, 1.0f + position.y, 1.0f + position.z, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, textures[0].toFloat(),
    0.0f + position.x, 1.0f + position.y, 0.0f + position.z, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, textures[0].toFloat(),
    0.0f + position.x, 1.0f + position.y, 1.0f + position.z, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, textures[0].toFloat(),

    //FRONT
    0.0f + position.x, 0.0f + position.y, 0.0f + position.z, 1.0f, 1.0f, 0.0f, 0.0f, -1.0f, textures[1].toFloat(),
    1.0f + position.x, 1.0f + position.y, 0.0f + position.z, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, textures[1].toFloat(),
    1.0f + position.x, 0.0f + position.y, 0.0f + position.z, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f, textures[1].toFloat(),
    1.0f + position.x, 1.0f + position.y, 0.0f + position.z, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, textures[1].toFloat(),
    0.0f + position.x, 0.0f + position.y, 0.0f + position.z, 1.0f, 1.0f, 0.0f, 0.0f, -1.0f, textures[1].toFloat(),
    0.0f + position.x, 1.0f + position.y, 0.0f + position.z, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f, textures[1].toFloat(),

    //BACK
    0.0f + position.x, 0.0f + position.y, 1.0f + position.z, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, textures[2].toFloat(),
    1.0f + position.x, 0.0f + position.y, 1.0f + position.z, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, textures[2].toFloat(),
    1.0f + position.x, 1.0f + position.y, 1.0f + position.z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, textures[2].toFloat(),
    1.0f + position.x, 1.0f + position.y, 1.0f + position.z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, textures[2].toFloat(),
    0.0f + position.x, 1.0f + position.y, 1.0f + position.z, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, textures[2].toFloat(),
    0.0f + position.x, 0.0f + position.y, 1.0f + position.z, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, textures[2].toFloat(),

    //LEFT
    0.0f + position.x, 1.0f + position.y, 1.0f + position.z, 1.0f, 0.0f, -1.0f, 0.0f, 0.0f, textures[3].toFloat(),
    0.0f + position.x, 1.0f + position.y, 0.0f + position.z, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, textures[3].toFloat(),
    0.0f + position.x, 0.0f + position.y, 0.0f + position.z, 0.0f, 1.0f, -1.0f, 0.0f, 0.0f, textures[3].toFloat(),
    0.0f + position.x, 0.0f + position.y, 0.0f + position.z, 0.0f, 1.0f, -1.0f, 0.0f, 0.0f, textures[3].toFloat(),
    0.0f + position.x, 0.0f + position.y, 1.0f + position.z, 1.0f, 1.0f, -1.0f, 0.0f, 0.0f, textures[3].toFloat(),
    0.0f + position.x, 1.0f + position.y, 1.0f + position.z, 1.0f, 0.0f, -1.0f, 0.0f, 0.0f, textures[3].toFloat(),

    //RIGHT
    1.0f + position.x, 1.0f + position.y, 1.0f + position.z, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, textures[4].toFloat(),
    1.0f + position.x, 0.0f + position.y, 0.0f + position.z, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, textures[4].toFloat(),
    1.0f + position.x, 1.0f + position.y, 0.0f + position.z, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, textures[4].toFloat(),
    1.0f + position.x, 0.0f + position.y, 0.0f + position.z, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, textures[4].toFloat(),
    1.0f + position.x, 1.0f + position.y, 1.0f + position.z, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, textures[4].toFloat(),
    1.0f + position.x, 0.0f + position.y, 1.0f + position.z, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, textures[4].toFloat(),

    //BOTTOM
    0.0f + position.x, 0.0f + position.y, 0.0f + position.z, 0.0f, 1.0f, 0.0f, -1.0f, 0.0f, textures[5].toFloat(),
    1.0f + position.x, 0.0f + position.y, 0.0f + position.z, 1.0f, 1.0f, 0.0f, -1.0f, 0.0f, textures[5].toFloat(),
    1.0f + position.x, 0.0f + position.y, 1.0f + position.z, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, textures[5].toFloat(),
    1.0f + position.x, 0.0f + position.y, 1.0f + position.z, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, textures[5].toFloat(),
    0.0f + position.x, 0.0f + position.y, 1.0f + position.z, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, textures[5].toFloat(),
    0.0f + position.x, 0.0f + position.y, 0.0f + position.z, 0.0f, 1.0f, 0.0f, -1.0f, 0.0f, textures[5].toFloat(),
)

class Chunk(
    val position: Vector3i,
    val blocks: Array<Byte> = Array(4096) { BlockType.AIR.id }
) {
    private var vaoID = 0
    private var vertexCount = 0

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
        val vertices = concatenateFloatArray(blocks
        .mapIndexed { index, value ->
            Block(BlockType.getByID(value), indexToXYZ(index))
        }.filter {
            wb -> wb.type != BlockType.AIR && isVisible(wb, this)
        }.map {
            getVertices(it.position, it.type.textures)
        })

        vertexCount = vertices.size / 9

        vaoID = GL30C.glGenVertexArrays()

        val vbo = GL15C.glGenBuffers()

        GL30C.glBindVertexArray(vaoID)

        val verticesBuffer = BufferUtils.createFloatBuffer(vertices.size)
        (verticesBuffer.put(vertices) as Buffer).flip()

        GL15C.glBindBuffer(GL15C.GL_ARRAY_BUFFER, vbo)
        GL15C.glBufferData(GL15C.GL_ARRAY_BUFFER, verticesBuffer, GL15C.GL_STATIC_DRAW)

        // Pos
        GL20C.glVertexAttribPointer(0, 3, GL11C.GL_FLOAT, false, 36, 0)
        GL20C.glEnableVertexAttribArray(0)

        // UV
        GL20C.glVertexAttribPointer(1, 2, GL11C.GL_FLOAT, false, 36, 12)
        GL20C.glEnableVertexAttribArray(1)

        // Norm
        GL20C.glVertexAttribPointer(2, 3, GL11C.GL_FLOAT, false, 36, 20)
        GL20C.glEnableVertexAttribArray(2)

        // Tex
        GL20C.glVertexAttribPointer(3, 1, GL11C.GL_FLOAT, false, 36, 32)
        GL20C.glEnableVertexAttribArray(3)

        GL15C.glBindBuffer(GL15C.GL_ARRAY_BUFFER, 0)
    }


    fun render() {
        glBindVertexArray(vaoID)
        glDrawArrays(GL_TRIANGLES, 0, vertexCount)
    }
}