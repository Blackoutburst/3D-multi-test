package dev.blackoutburst.game.world

import dev.blackoutburst.game.Main
import dev.blackoutburst.game.maths.*
import dev.blackoutburst.game.utils.concatenateIntArray
import dev.blackoutburst.game.utils.main
import dev.blackoutburst.game.utils.stack
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL30.*
import java.nio.Buffer
import java.nio.IntBuffer

private fun packData(
    x: Int,
    y: Int,
    z: Int,
    u: Int,
    v: Int,
    n: Int,
    t: Int,
):Int = (x and 31) or
        ((y and 31) shl 5) or
        ((z and 31) shl 10) or
        ((u and 31) shl 15) or
        ((v and 31) shl 20) or
        ((n and 7) shl 25) or
        ((t and 15) shl 28)

private fun getFace(
    pos1: Vector3i,
    pos2: Vector3i,
    pos3: Vector3i,
    pos4: Vector3i,
    uv1: Vector2i,
    uv2: Vector2i,
    uv3: Vector2i,
    uv4: Vector2i,
    normal: Int,
    texture: Int
): IntArray = intArrayOf(
        packData(pos1.x, pos1.y, pos1.z, uv1.x, uv1.y, normal, texture),
        packData(pos2.x, pos2.y, pos2.z, uv2.x, uv2.y, normal, texture),
        packData(pos3.x, pos3.y, pos3.z, uv3.x, uv3.y, normal, texture),
        packData(pos4.x, pos4.y, pos4.z, uv4.x, uv4.y, normal, texture),
    )


/*
NORMALS MAP

private fun getFace(
    pos1: Vector3f,
    pos2: Vector3f,
    pos3: Vector3f,
    pos4: Vector3f,
    uv1: Vector2f,
    uv2: Vector2f,
    uv3: Vector2f,
    uv4: Vector2f,
    normal: Float,
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
        pos1.x, pos1.y, pos1.z, uv1.x, uv1.y, normal, texture, tangent1.x, tangent1.y, tangent1.z, bitangent1.x, bitangent1.y, bitangent1.z,
        pos2.x, pos2.y, pos2.z, uv2.x, uv2.y, normal, texture, tangent1.x, tangent1.y, tangent1.z, bitangent1.x, bitangent1.y, bitangent1.z,
        pos3.x, pos3.y, pos3.z, uv3.x, uv3.y, normal, texture, tangent2.x, tangent2.y, tangent2.z, bitangent2.x, bitangent2.y, bitangent2.z,
        pos4.x, pos4.y, pos4.z, uv4.x, uv4.y, normal, texture, tangent2.x, tangent2.y, tangent2.z, bitangent2.x, bitangent2.y, bitangent2.z,
    )
}

 */

private fun getVertices(position: Vector3i, textures: Array<Int>, faces: Array<Boolean>, scale: Int = 1): List<Int> {
    val topPos1 = Vector3i((scale * 0 + position.x), (scale * 1 + position.y), (scale * 0 + position.z))
    val topPos2 = Vector3i((scale * 1 + position.x), (scale * 1 + position.y), (scale * 0 + position.z))
    val topPos3 = Vector3i((scale * 1 + position.x), (scale * 1 + position.y), (scale * 1 + position.z))
    val topPos4 = Vector3i((scale * 0 + position.x), (scale * 1 + position.y), (scale * 1 + position.z))
    val topUv1 = Vector2i((0 * scale), (0 * scale))
    val topUv2 = Vector2i((1 * scale), (0 * scale))
    val topUv3 = Vector2i((1 * scale), (1 * scale))
    val topUv4 = Vector2i((0 * scale), (1 * scale))
    val topNormal = 0
    val topTexture = textures[0]

    val top = getFace(topPos1, topPos2, topPos3, topPos4, topUv1, topUv2, topUv3, topUv4, topNormal, topTexture)

    val frontPos1 = Vector3i((scale * 0 + position.x), (scale * 0 + position.y), (scale * 0 + position.z))
    val frontPos2 = Vector3i((scale * 1 + position.x), (scale * 0 + position.y), (scale * 0 + position.z))
    val frontPos3 = Vector3i((scale * 1 + position.x), (scale * 1 + position.y), (scale * 0 + position.z))
    val frontPos4 = Vector3i((scale * 0 + position.x), (scale * 1 + position.y), (scale * 0 + position.z))
    val frontUv1 = Vector2i((1 * scale), (1 * scale))
    val frontUv2 = Vector2i((0 * scale), (1 * scale))
    val frontUv3 = Vector2i((0 * scale), (0 * scale))
    val frontUv4 = Vector2i((1 * scale), (0 * scale))
    val frontNormal = 1
    val frontTexture = textures[1]

    val front = getFace(frontPos1, frontPos2, frontPos3, frontPos4, frontUv1, frontUv2, frontUv3, frontUv4, frontNormal, frontTexture)

    val backPos1 = Vector3i((scale * 0 + position.x), (scale * 0 + position.y), (scale * 1 + position.z))
    val backPos2 = Vector3i((scale * 1 + position.x), (scale * 0 + position.y), (scale * 1 + position.z))
    val backPos3 = Vector3i((scale * 1 + position.x), (scale * 1 + position.y), (scale * 1 + position.z))
    val backPos4 = Vector3i((scale * 0 + position.x), (scale * 1 + position.y), (scale * 1 + position.z))
    val backUv1 = Vector2i((0 * scale), (1 * scale))
    val backUv2 = Vector2i((1 * scale), (1 * scale))
    val backUv3 = Vector2i((1 * scale), (0 * scale))
    val backUv4 = Vector2i((0 * scale), (0 * scale))
    val backNormal = 2
    val backTexture = textures[2]

    val back = getFace(backPos1, backPos2, backPos3, backPos4, backUv1, backUv2, backUv3, backUv4, backNormal, backTexture)

    val leftPos1 = Vector3i((scale * 0 + position.x), (scale * 0 + position.y), (scale * 0 + position.z))
    val leftPos2 = Vector3i((scale * 0 + position.x), (scale * 1 + position.y), (scale * 0 + position.z))
    val leftPos3 = Vector3i((scale * 0 + position.x), (scale * 1 + position.y), (scale * 1 + position.z))
    val leftPos4 = Vector3i((scale * 0 + position.x), (scale * 0 + position.y), (scale * 1 + position.z))
    val leftUv1 = Vector2i((1 * scale), (1 * scale))
    val leftUv2 = Vector2i((1 * scale), (0 * scale))
    val leftUv3 = Vector2i((0 * scale), (0 * scale))
    val leftUv4 = Vector2i((0 * scale), (1 * scale))
    val leftNormal = 3
    val leftTexture = textures[3]

    val left = getFace(leftPos1, leftPos2, leftPos3, leftPos4, leftUv1, leftUv2, leftUv3, leftUv4, leftNormal, leftTexture)

    val rightPos1 = Vector3i((scale * 1 + position.x), (scale * 0 + position.y), (scale * 0 + position.z))
    val rightPos2 = Vector3i((scale * 1 + position.x), (scale * 1 + position.y), (scale * 0 + position.z))
    val rightPos3 = Vector3i((scale * 1 + position.x), (scale * 1 + position.y), (scale * 1 + position.z))
    val rightPos4 = Vector3i((scale * 1 + position.x), (scale * 0 + position.y), (scale * 1 + position.z))
    val rightUv1 = Vector2i((0 * scale), (1 * scale))
    val rightUv2 = Vector2i((0 * scale), (0 * scale))
    val rightUv3 = Vector2i((1 * scale), (0 * scale))
    val rightUv4 = Vector2i((1 * scale), (1 * scale))
    val rightNormal = 4
    val rightTexture = textures[4]

    val right = getFace(rightPos1, rightPos2, rightPos3, rightPos4, rightUv1, rightUv2, rightUv3, rightUv4, rightNormal, rightTexture)

    val bottomPos1 = Vector3i((scale * 0 + position.x), (scale * 0 + position.y), (scale * 0 + position.z))
    val bottomPos2 = Vector3i((scale * 1 + position.x), (scale * 0 + position.y), (scale * 0 + position.z))
    val bottomPos3 = Vector3i((scale * 1 + position.x), (scale * 0 + position.y), (scale * 1 + position.z))
    val bottomPos4 = Vector3i((scale * 0 + position.x), (scale * 0 + position.y), (scale * 1 + position.z))
    val bottomUv1 = Vector2i((0 * scale), (1 * scale))
    val bottomUv2 = Vector2i((1 * scale), (1 * scale))
    val bottomUv3 = Vector2i((1 * scale), (0 * scale))
    val bottomUv4 = Vector2i((0 * scale), (0 * scale))
    val bottomNormal = 5
    val bottomTexture = textures[5]

    val bottom = getFace(bottomPos1, bottomPos2, bottomPos3, bottomPos4, bottomUv1, bottomUv2, bottomUv3, bottomUv4, bottomNormal, bottomTexture)

    val result = mutableListOf<Int>()
    if (faces[0]) result.addAll(top.asIterable())
    if (faces[1]) result.addAll(front.asIterable())
    if (faces[2]) result.addAll(back.asIterable())
    if (faces[3]) result.addAll(left.asIterable())
    if (faces[4]) result.addAll(right.asIterable())
    if (faces[5]) result.addAll(bottom.asIterable())

    return result
}

private fun getIndices(offset: Int, faces: Array<Boolean>): List<Int> {
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

    return indices
}

data class ChunkBlock(
    val type: BlockType,
    val position: Vector3i,
    val vertPosition: Vector3i,
    var faces: Array<Boolean>
)

class Chunk(
    val position: Vector3i,
    var blocks: Array<Byte> = Array(4096) { BlockType.AIR.id }
) {
    var vaoID = 0
    var vboID = 0
    var eboID = 0
    var indexCount = 0
    var blockCount = 0

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

    fun indexToXYZ(index: Int): Vector3i = Vector3i(index % 16, (index / 16) % 16, (index / (16 * 16)) % 16)

    fun indexToXYZPosition(index: Int): Vector3i = Vector3i(index % 16, (index / 16) % 16, (index / (16 * 16)) % 16) + this.position

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
        Main.world.chunkUpdate.incrementAndGet()

        val isMonoType = isMonoType()
        if (isMonoType && BlockType.getByID(blocks[0]) == BlockType.AIR) {
            Main.world.removeChunk(this)
            return
        }

        val vertices: IntArray
        val indices: IntArray

        if (isMonoType) {
            val faces = getVisibleFaces()
            vertices = getVertices(Vector3i(0), BlockType.getByID(blocks[0]).textures, faces, 16).toIntArray()
            indices = getIndices(0, faces).toIntArray()
        } else {
            val filteredBlocks = blocks
                .mapIndexed { index, value ->
                    ChunkBlock(
                        BlockType.getByID(value),
                        indexToXYZPosition(index),
                        indexToXYZ(index),
                        Array(6) { true })
                }.filter { b ->
                    b.type != BlockType.AIR && isVisibleBlock(b, this)
                }.map {
                    it.faces = if (it.type.transparent) Array(6) { true } else getVisibleFaces(it, this)
                    it
                }
            blockCount = filteredBlocks.size
            vertices = filteredBlocks.flatMap { getVertices(it.vertPosition, it.type.textures, it.faces) }.toIntArray()
            var iIndex = 0
            indices = filteredBlocks.flatMap { b ->
                val inds = getIndices(iIndex, b.faces)
                iIndex += (b.faces.count { it } * 4)

                inds
            }.toIntArray()
        }

        indexCount = indices.size

        main {
            var vertexBuffer: IntBuffer? = null
            var indexBuffer: IntBuffer? = null
            stack {
                vertexBuffer = BufferUtils.createIntBuffer(vertices.size)
                (vertexBuffer!!.put(vertices) as Buffer).flip()

                indexBuffer = BufferUtils.createIntBuffer(indices.size)
                (indexBuffer!!.put(indices) as Buffer).flip()
            }

            computeVAO(vertexBuffer!!, indexBuffer!!)
        }
    }

    fun computeVAO(vertexBuffer: IntBuffer, indexBuffer: IntBuffer) {
        if (vaoID == 0) {
            vaoID = glGenVertexArrays()
            vboID = glGenBuffers()
            eboID = glGenBuffers()
        }

        glBindVertexArray(vaoID)

        glBindBuffer(GL_ARRAY_BUFFER, vboID)
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW)

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW)

        glEnableVertexAttribArray(0)
        glVertexAttribIPointer(0, 1, GL_INT, 4, 0)

        glBindVertexArray(0)
        Main.world.chunkUpdate.decrementAndGet()
    }

    fun render() {
        glBindVertexArray(vaoID)
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0)
        glBindVertexArray(0)
    }
}