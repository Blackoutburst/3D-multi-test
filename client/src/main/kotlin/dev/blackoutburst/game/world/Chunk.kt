package dev.blackoutburst.game.world

import dev.blackoutburst.game.Main
import dev.blackoutburst.game.maths.Vector2f
import dev.blackoutburst.game.maths.Vector3f
import dev.blackoutburst.game.maths.Vector3i
import dev.blackoutburst.game.utils.concatenateFloatArray
import dev.blackoutburst.game.utils.concatenateIntArray
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.*
import org.lwjgl.opengl.GL30.*
import java.nio.Buffer

private fun getFace(
    pos1: Vector3f,
    pos2: Vector3f,
    pos3: Vector3f,
    pos4: Vector3f,
    uv1: Vector2f,
    uv2: Vector2f,
    uv3: Vector2f,
    uv4: Vector2f,
    normal: Vector3f,
    texture: Float
): FloatArray {

    var edge1 = pos2 - pos1
    var edge2 = pos3 - pos1
    var deltaUV1 = uv2 - uv1
    var deltaUV2 = uv3 - uv1

    val tangent1 = Vector3f()
    val bitangent1 = Vector3f()
    val tangent2 = Vector3f()
    val bitangent2 = Vector3f()

    var f: Float = 1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y)

    tangent1.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x)
    tangent1.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y)
    tangent1.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z)

    bitangent1.x = f * (-deltaUV2.x * edge1.x + deltaUV1.x * edge2.x)
    bitangent1.y = f * (-deltaUV2.x * edge1.y + deltaUV1.x * edge2.y)
    bitangent1.z = f * (-deltaUV2.x * edge1.z + deltaUV1.x * edge2.z)


    edge1 = pos3 - pos1
    edge2 = pos4 - pos1
    deltaUV1 = uv3 - uv1
    deltaUV2 = uv4 - uv1

    f = 1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y)

    tangent2.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x)
    tangent2.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y)
    tangent2.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z)


    bitangent2.x = f * (-deltaUV2.x * edge1.x + deltaUV1.x * edge2.x)
    bitangent2.y = f * (-deltaUV2.x * edge1.y + deltaUV1.x * edge2.y)
    bitangent2.z = f * (-deltaUV2.x * edge1.z + deltaUV1.x * edge2.z)

    return floatArrayOf(
        pos1.x, pos1.y, pos1.z, uv1.x, uv1.y, normal.x, normal.y, normal.z, texture, tangent1.x, tangent1.y, tangent1.z, bitangent1.x, bitangent1.y, bitangent1.z,
        pos2.x, pos2.y, pos2.z, uv2.x, uv2.y, normal.x, normal.y, normal.z, texture, tangent1.x, tangent1.y, tangent1.z, bitangent1.x, bitangent1.y, bitangent1.z,
        pos3.x, pos3.y, pos3.z, uv3.x, uv3.y, normal.x, normal.y, normal.z, texture, tangent2.x, tangent2.y, tangent2.z, bitangent2.x, bitangent2.y, bitangent2.z,
        pos4.x, pos4.y, pos4.z, uv4.x, uv4.y, normal.x, normal.y, normal.z, texture, tangent2.x, tangent2.y, tangent2.z, bitangent2.x, bitangent2.y, bitangent2.z,
    )
}

private fun getVertices(position: Vector3i, textures: Array<Int>, faces: Array<Boolean>, scale: Float = 1.0f): FloatArray {
    val topPos1 = Vector3f(scale * 0.0f + position.x, scale * 1.0f + position.y, scale * 0.0f + position.z)
    val topPos2 = Vector3f(scale * 1.0f + position.x, scale * 1.0f + position.y, scale * 0.0f + position.z)
    val topPos3 = Vector3f(scale * 1.0f + position.x, scale * 1.0f + position.y, scale * 1.0f + position.z)
    val topPos4 = Vector3f(scale * 0.0f + position.x, scale * 1.0f + position.y, scale * 1.0f + position.z)
    val topUv1 = Vector2f(0.0f * scale, 0.0f * scale)
    val topUv2 = Vector2f(1.0f * scale, 0.0f * scale)
    val topUv3 = Vector2f(1.0f * scale, 1.0f * scale)
    val topUv4 = Vector2f(0.0f * scale, 1.0f * scale)
    val topNormal = Vector3f(0.0f, 1.0f, 0.0f)
    val topTexture = textures[0].toFloat()

    val top = getFace(topPos1, topPos2, topPos3, topPos4, topUv1, topUv2, topUv3, topUv4, topNormal, topTexture)

    val frontPos1 = Vector3f(scale * 0.0f + position.x, scale * 0.0f + position.y, scale * 0.0f + position.z)
    val frontPos2 = Vector3f(scale * 1.0f + position.x, scale * 0.0f + position.y, scale * 0.0f + position.z)
    val frontPos3 = Vector3f(scale * 1.0f + position.x, scale * 1.0f + position.y, scale * 0.0f + position.z)
    val frontPos4 = Vector3f(scale * 0.0f + position.x, scale * 1.0f + position.y, scale * 0.0f + position.z)
    val frontUv1 = Vector2f(1.0f * scale, 1.0f * scale)
    val frontUv2 = Vector2f(0.0f * scale, 1.0f * scale)
    val frontUv3 = Vector2f(0.0f * scale, 0.0f * scale)
    val frontUv4 = Vector2f(1.0f * scale, 0.0f * scale)
    val frontNormal = Vector3f(0.0f, 0.0f, -1.0f)
    val frontTexture = textures[1].toFloat()

    val front = getFace(frontPos1, frontPos2, frontPos3, frontPos4, frontUv1, frontUv2, frontUv3, frontUv4, frontNormal, frontTexture)

    val backPos1 = Vector3f(scale * 0.0f + position.x, scale * 0.0f + position.y, scale * 1.0f + position.z)
    val backPos2 = Vector3f(scale * 1.0f + position.x, scale * 0.0f + position.y, scale * 1.0f + position.z)
    val backPos3 = Vector3f(scale * 1.0f + position.x, scale * 1.0f + position.y, scale * 1.0f + position.z)
    val backPos4 = Vector3f(scale * 0.0f + position.x, scale * 1.0f + position.y, scale * 1.0f + position.z)
    val backUv1 = Vector2f(0.0f * scale, 1.0f * scale)
    val backUv2 = Vector2f(1.0f * scale, 1.0f * scale)
    val backUv3 = Vector2f(1.0f * scale, 0.0f * scale)
    val backUv4 = Vector2f(0.0f * scale, 0.0f * scale)
    val backNormal = Vector3f(0.0f, 0.0f, 1.0f)
    val backTexture = textures[2].toFloat()

    val back = getFace(backPos1, backPos2, backPos3, backPos4, backUv1, backUv2, backUv3, backUv4, backNormal, backTexture)

    val leftPos1 = Vector3f(scale * 0.0f + position.x, scale * 0.0f + position.y, scale * 0.0f + position.z)
    val leftPos2 = Vector3f(scale * 0.0f + position.x, scale * 1.0f + position.y, scale * 0.0f + position.z)
    val leftPos3 = Vector3f(scale * 0.0f + position.x, scale * 1.0f + position.y, scale * 1.0f + position.z)
    val leftPos4 = Vector3f(scale * 0.0f + position.x, scale * 0.0f + position.y, scale * 1.0f + position.z)
    val leftUv1 = Vector2f(1.0f * scale, 1.0f * scale)
    val leftUv2 = Vector2f(1.0f * scale, 0.0f * scale)
    val leftUv3 = Vector2f(0.0f * scale, 0.0f * scale)
    val leftUv4 = Vector2f(0.0f * scale, 1.0f * scale)
    val leftNormal = Vector3f(-1.0f, 0.0f, 0.0f)
    val leftTexture = textures[3].toFloat()

    val left = getFace(leftPos1, leftPos2, leftPos3, leftPos4, leftUv1, leftUv2, leftUv3, leftUv4, leftNormal, leftTexture)

    val rightPos1 = Vector3f(scale * 1.0f + position.x, scale * 0.0f + position.y, scale * 0.0f + position.z)
    val rightPos2 = Vector3f(scale * 1.0f + position.x, scale * 1.0f + position.y, scale * 0.0f + position.z)
    val rightPos3 = Vector3f(scale * 1.0f + position.x, scale * 1.0f + position.y, scale * 1.0f + position.z)
    val rightPos4 = Vector3f(scale * 1.0f + position.x, scale * 0.0f + position.y, scale * 1.0f + position.z)
    val rightUv1 = Vector2f(0.0f * scale, 1.0f * scale)
    val rightUv2 = Vector2f(0.0f * scale, 0.0f * scale)
    val rightUv3 = Vector2f(1.0f * scale, 0.0f * scale)
    val rightUv4 = Vector2f(1.0f * scale, 1.0f * scale)
    val rightNormal = Vector3f(1.0f, 0.0f, 0.0f)
    val rightTexture = textures[4].toFloat()

    val right = getFace(rightPos1, rightPos2, rightPos3, rightPos4, rightUv1, rightUv2, rightUv3, rightUv4, rightNormal, rightTexture)

    val bottomPos1 = Vector3f(scale * 0.0f + position.x, scale * 0.0f + position.y, scale * 0.0f + position.z)
    val bottomPos2 = Vector3f(scale * 1.0f + position.x, scale * 0.0f + position.y, scale * 0.0f + position.z)
    val bottomPos3 = Vector3f(scale * 1.0f + position.x, scale * 0.0f + position.y, scale * 1.0f + position.z)
    val bottomPos4 = Vector3f(scale * 0.0f + position.x, scale * 0.0f + position.y, scale * 1.0f + position.z)
    val bottomUv1 = Vector2f(0.0f * scale, 1.0f * scale)
    val bottomUv2 = Vector2f(1.0f * scale, 1.0f * scale)
    val bottomUv3 = Vector2f(1.0f * scale, 0.0f * scale)
    val bottomUv4 = Vector2f(0.0f * scale, 0.0f * scale)
    val bottomNormal = Vector3f(1.0f, 0.0f, 0.0f)
    val bottomTexture = textures[5].toFloat()

    val bottom = getFace(bottomPos1, bottomPos2, bottomPos3, bottomPos4, bottomUv1, bottomUv2, bottomUv3, bottomUv4, bottomNormal, bottomTexture)

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

        vertexCount = vertices.size / 15

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
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 60, 0)
        glEnableVertexAttribArray(0)

        // UV
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 60, 12)
        glEnableVertexAttribArray(1)

        // Norm
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 60, 20)
        glEnableVertexAttribArray(2)

        // Tex
        glVertexAttribPointer(3, 1, GL_FLOAT, false, 60, 32)
        glEnableVertexAttribArray(3)

        // Tangent
        glVertexAttribPointer(4, 3, GL_FLOAT, false, 60, 36)
        glEnableVertexAttribArray(4)

        // Bi Tangent
        glVertexAttribPointer(5, 3, GL_FLOAT, false, 60, 48)
        glEnableVertexAttribArray(5)

        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
    }


    fun render() {
        glBindVertexArray(vaoID)
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0)
    }
}