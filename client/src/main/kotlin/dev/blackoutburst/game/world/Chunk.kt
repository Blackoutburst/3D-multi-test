package dev.blackoutburst.game.world

import dev.blackoutburst.game.Main
import dev.blackoutburst.game.maths.Vector3i
import dev.blackoutburst.game.utils.concatenateFloatArray
import dev.blackoutburst.game.utils.concatenateIntArray
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.*
import org.lwjgl.opengl.GL30.*
import java.nio.Buffer

private fun getVertices(position: Vector3i, textures: Array<Int>, faces: Array<Boolean>, scale: Float = 1.0f): FloatArray {
    val top = floatArrayOf(
        scale * 0.0f + position.x, scale * 1.0f + position.y, scale * 0.0f + position.z, 0.0f* scale, 0.0f* scale, 0.0f, 1.0f, 0.0f, textures[0].toFloat(),
        scale * 1.0f + position.x, scale * 1.0f + position.y, scale * 0.0f + position.z, 1.0f* scale, 0.0f* scale, 0.0f, 1.0f, 0.0f, textures[0].toFloat(),
        scale * 1.0f + position.x, scale * 1.0f + position.y, scale * 1.0f + position.z, 1.0f* scale, 1.0f* scale, 0.0f, 1.0f, 0.0f, textures[0].toFloat(),
        scale * 0.0f + position.x, scale * 1.0f + position.y, scale * 1.0f + position.z, 0.0f* scale, 1.0f* scale, 0.0f, 1.0f, 0.0f, textures[0].toFloat(),
    )

    val front = floatArrayOf(
        scale * 0.0f + position.x, scale * 0.0f + position.y, scale * 0.0f + position.z, 1.0f* scale, 1.0f* scale, 0.0f, 0.0f, -1.0f, textures[1].toFloat(),
        scale * 1.0f + position.x, scale * 0.0f + position.y, scale * 0.0f + position.z, 0.0f* scale, 1.0f* scale, 0.0f, 0.0f, -1.0f, textures[1].toFloat(),
        scale * 1.0f + position.x, scale * 1.0f + position.y, scale * 0.0f + position.z, 0.0f* scale, 0.0f* scale, 0.0f, 0.0f, -1.0f, textures[1].toFloat(),
        scale * 0.0f + position.x, scale * 1.0f + position.y, scale * 0.0f + position.z, 1.0f* scale, 0.0f* scale, 0.0f, 0.0f, -1.0f, textures[1].toFloat(),
    )

    val back = floatArrayOf(
        scale * 0.0f + position.x, scale * 0.0f + position.y, scale * 1.0f + position.z, 0.0f* scale, 1.0f* scale, 0.0f, 0.0f, 1.0f, textures[2].toFloat(),
        scale * 1.0f + position.x, scale * 0.0f + position.y, scale * 1.0f + position.z, 1.0f* scale, 1.0f* scale, 0.0f, 0.0f, 1.0f, textures[2].toFloat(),
        scale * 1.0f + position.x, scale * 1.0f + position.y, scale * 1.0f + position.z, 1.0f* scale, 0.0f* scale, 0.0f, 0.0f, 1.0f, textures[2].toFloat(),
        scale * 0.0f + position.x, scale * 1.0f + position.y, scale * 1.0f + position.z, 0.0f* scale, 0.0f* scale, 0.0f, 0.0f, 1.0f, textures[2].toFloat(),
    )

    val left = floatArrayOf(
        scale * 0.0f + position.x, scale * 0.0f + position.y, scale * 0.0f + position.z, 1.0f* scale, 1.0f* scale, -1.0f, 0.0f, 0.0f, textures[3].toFloat(),
        scale * 0.0f + position.x, scale * 1.0f + position.y, scale * 0.0f + position.z, 1.0f* scale, 0.0f* scale, -1.0f, 0.0f, 0.0f, textures[3].toFloat(),
        scale * 0.0f + position.x, scale * 1.0f + position.y, scale * 1.0f + position.z, 0.0f* scale, 0.0f* scale, -1.0f, 0.0f, 0.0f, textures[3].toFloat(),
        scale * 0.0f + position.x, scale * 0.0f + position.y, scale * 1.0f + position.z, 0.0f* scale, 1.0f* scale, -1.0f, 0.0f, 0.0f, textures[3].toFloat(),
    )

    val right = floatArrayOf(
        scale * 1.0f + position.x, scale * 0.0f + position.y, scale * 0.0f + position.z, 0.0f* scale, 1.0f* scale, 1.0f, 0.0f, 0.0f, textures[4].toFloat(),
        scale * 1.0f + position.x, scale * 1.0f + position.y, scale * 0.0f + position.z, 0.0f* scale, 0.0f* scale, 1.0f, 0.0f, 0.0f, textures[4].toFloat(),
        scale * 1.0f + position.x, scale * 1.0f + position.y, scale * 1.0f + position.z, 1.0f* scale, 0.0f* scale, 1.0f, 0.0f, 0.0f, textures[4].toFloat(),
        scale * 1.0f + position.x, scale * 0.0f + position.y, scale * 1.0f + position.z, 1.0f* scale, 1.0f* scale, 1.0f, 0.0f, 0.0f, textures[4].toFloat(),
    )

    val bottom = floatArrayOf(
        scale * 0.0f + position.x, scale * 0.0f + position.y, scale * 0.0f + position.z, 0.0f* scale, 1.0f* scale, 0.0f, -1.0f, 0.0f, textures[5].toFloat(),
        scale * 1.0f + position.x, scale * 0.0f + position.y, scale * 0.0f + position.z, 1.0f* scale, 1.0f* scale, 0.0f, -1.0f, 0.0f, textures[5].toFloat(),
        scale * 1.0f + position.x, scale * 0.0f + position.y, scale * 1.0f + position.z, 1.0f* scale, 0.0f* scale, 0.0f, -1.0f, 0.0f, textures[5].toFloat(),
        scale * 0.0f + position.x, scale * 0.0f + position.y, scale * 1.0f + position.z, 0.0f* scale, 0.0f* scale, 0.0f, -1.0f, 0.0f, textures[5].toFloat(),
    )

    val result = mutableListOf<Float>()
    if (faces[0]) result.addAll(top.asIterable())
    if (faces[1]) result.addAll(front.asIterable())
    if (faces[2]) result.addAll(back.asIterable())
    if (faces[3]) result.addAll(left.asIterable())
    if (faces[4]) result.addAll(right.asIterable())
    if (faces[5]) result.addAll(bottom.asIterable())

    return result.toFloatArray()
}

private fun getIndices(offset: Int, faces: Array<Boolean>): IntArray {
    val indices = mutableListOf<Int>()
    var internalOffset = 0

    if (faces[0]) {
        indices.addAll(intArrayOf(0 + offset, 2 + offset, 1 + offset, 0 + offset, 3 + offset, 2 + offset).asIterable())
        internalOffset += 4
    }
    if (faces[1]) {
        indices.addAll(intArrayOf(0 + offset, 2 + offset, 1 + offset, 0 + offset, 3 + offset, 2 + offset).map { it + internalOffset }.asIterable())
        internalOffset += 4
    }
    if (faces[2]) {
        indices.addAll(intArrayOf(1 + offset, 2 + offset, 0 + offset, 2 + offset, 3 + offset, 0 + offset).map { it + internalOffset }.asIterable())
        internalOffset += 4
    }
    if (faces[3]) {
        indices.addAll(intArrayOf(0 + offset, 2 + offset, 1 + offset, 0 + offset, 3 + offset, 2 + offset).map { it + internalOffset }.asIterable())
        internalOffset += 4
    }
    if (faces[4]) {
        indices.addAll(intArrayOf(1 + offset, 2 + offset, 0 + offset, 2 + offset, 3 + offset, 0 + offset).map { it + internalOffset }.asIterable())
        internalOffset += 4
    }
    if (faces[5]) {
        indices.addAll(intArrayOf(1 + offset, 2 + offset, 0 + offset, 2 + offset, 3 + offset, 0 + offset).map { it + internalOffset }.asIterable())
        internalOffset += 4
    }

    return indices.toIntArray()
}

data class ChunkBlock(
    val type: BlockType,
    val position: Vector3i,
    var faces: Array<Boolean>
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

    fun blockAt(chunk: Chunk, x: Int, y: Int, z: Int): Byte = chunk.blocks[xyzToIndex(x, y, z)]

    fun xyzToIndex(x: Int, y: Int, z: Int): Int = x + 16 * (y + 16 * z)

    fun isAir(x: Int, y: Int, z: Int, currentType: BlockType): Boolean {
        return if (isWithinBounds(x, y, z)) {
            val type = BlockType.getByID(blockAt(this, x, y, z))

            (type == BlockType.AIR || type.transparent && !currentType.transparent)
        } else {
            val type = Main.world.getBlockAt(Vector3i(x + this.position.x, y + this.position.y, z + this.position.z))?.type
            return if (type == null) true
            else (type == BlockType.AIR || type.transparent && !currentType.transparent)
        }
    }

    fun isWithinBounds(x: Int, y: Int, z: Int): Boolean = x in 0 until 16 && y in 0 until 16 && z in 0 until 16

    fun indexToXYZ(index: Int): Vector3i = Vector3i(index % 16, (index / 16) % 16, (index / (16 * 16)) % 16) + this.position

    fun isVisibleBlock(block: ChunkBlock, chunk: Chunk): Boolean {
        val pos = block.position - chunk.position
        val type = block.type
        val x = pos.x
        val y = pos.y
        val z = pos.z

        return chunk.isAir(x + 1, y, z, type) || chunk.isAir(x - 1, y, z, type) ||
                chunk.isAir(x, y + 1, z, type) || chunk.isAir(x, y - 1, z, type) ||
                chunk.isAir(x, y, z + 1, type) || chunk.isAir(x, y, z - 1, type)
    }

    fun isMonoType(): Boolean = this.blocks.all { it == this.blocks.first() }

    fun getVisibleFaces(): Array<Boolean> {
        val faces = Array(6) { false }

        for (x in 0 until 16) {
            for (z in 0 until 16) {
                val t = Main.world.getBlockAt(Vector3i(this.position.x + x, this.position.y - 1, this.position.z + z))
                if (t == null || t.type == BlockType.AIR) faces[5] = true
                val b = Main.world.getBlockAt(Vector3i(this.position.x + x, this.position.y + 16, this.position.z + z))
                if (b == null || b.type == BlockType.AIR) faces[0] = true
            }
        }
        for (x in 0 until 16) {
            for (y in 0 until 16) {
                val b = Main.world.getBlockAt(Vector3i(this.position.x + x, this.position.y + y, this.position.z + 16))
                if (b == null || b.type == BlockType.AIR) faces[2] = true
                val f = Main.world.getBlockAt(Vector3i(this.position.x + x, this.position.y + y, this.position.z - 1))
                if (f == null || f.type == BlockType.AIR) faces[1] = true
            }
        }

        for (y in 0 until 16) {
            for (z in 0 until 16) {
                val l = Main.world.getBlockAt(Vector3i(this.position.x + 16, this.position.y + y, this.position.z + z))
                if (l == null || l.type == BlockType.AIR) faces[4] = true
                val r = Main.world.getBlockAt(Vector3i(this.position.x - 1, this.position.y + y, this.position.z + z))
                if (r == null || r.type == BlockType.AIR) faces[3] = true
            }
        }

        return faces
    }

    fun getVisibleFaces(block: ChunkBlock, chunk: Chunk): Array<Boolean> {
        val pos = block.position - chunk.position
        val type = block.type
        val x = pos.x
        val y = pos.y
        val z = pos.z

        return arrayOf(
            chunk.isAir(x, y + 1, z, type),
            chunk.isAir(x, y, z - 1, type),
            chunk.isAir(x, y, z + 1, type),
            chunk.isAir(x - 1, y, z, type),
            chunk.isAir(x + 1, y, z, type),
            chunk.isAir(x, y - 1, z, type),
        )
    }

    fun update() {
        if (isMonoType() && BlockType.getByID(blocks[0]) == BlockType.AIR) {
            Main.world.chunks.remove(this.position.toString())
            return
        }

        val vertices: FloatArray
        val indices: IntArray

        if (isMonoType()) {
            val faces = getVisibleFaces()
            vertices = getVertices(position, BlockType.getByID(blocks[0]).textures, faces, 16.0f)
            indices = getIndices(0, faces)
        } else {
            val filteredBlocks = blocks
                .mapIndexed { index, value ->
                    ChunkBlock(BlockType.getByID(value), indexToXYZ(index), Array(6) { true })
                }.filter { b ->
                    b.type != BlockType.AIR && isVisibleBlock(b, this)
                }.map {
                    it.faces = if (it.type.transparent) Array(6) { true } else getVisibleFaces(it, this)
                    it
                }

            vertices = concatenateFloatArray(filteredBlocks.map { getVertices(it.position, it.type.textures, it.faces) })
            var iIndex = 0
            indices = concatenateIntArray(filteredBlocks.map { b ->
                val inds = getIndices(iIndex, b.faces)
                iIndex += (b.faces.count { it } * 4)

                inds
            })
        }

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