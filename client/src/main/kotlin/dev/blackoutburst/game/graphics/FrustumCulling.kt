package dev.blackoutburst.game.graphics

import dev.blackoutburst.game.maths.Matrix
import dev.blackoutburst.game.maths.Vector4f

object FrustumCulling {
    val planes = Array(6) { Vector4f() }

    fun updateFrustum(projMatrix: Matrix, viewMatrix: Matrix) {
        val projCopy = Matrix(projMatrix)
        val viewCopy = Matrix(viewMatrix)
        val combinedMatrix = projCopy.mul(viewCopy)

        for (i in 0 until 6) {
            planes[i].set(
                combinedMatrix.m03 + combinedMatrix.get(i / 2) * if (i % 2 == 0) 1 else -1,
                combinedMatrix.m13 + combinedMatrix.get(4 + (i / 2) % 3) * if (i % 2 == 0) 1 else -1,
                combinedMatrix.m23 + combinedMatrix.get(8 + (i / 2) % 3) * if (i % 2 == 0) 1 else -1,
                combinedMatrix.m33 + combinedMatrix.get(12 + (i / 2) % 3) * if (i % 2 == 0) 1 else -1
            ).normalize()
        }
    }

    fun isBoxInFrustum(minX: Float, minY: Float, minZ: Float, maxX: Float, maxY: Float, maxZ: Float): Boolean {
        for (plane in planes) {
            if (plane.x * minX + plane.y * minY + plane.z * minZ + plane.w > 0) continue
            if (plane.x * maxX + plane.y * minY + plane.z * minZ + plane.w > 0) continue
            if (plane.x * minX + plane.y * maxY + plane.z * minZ + plane.w > 0) continue
            if (plane.x * maxX + plane.y * maxY + plane.z * minZ + plane.w > 0) continue
            if (plane.x * minX + plane.y * minY + plane.z * maxZ + plane.w > 0) continue
            if (plane.x * maxX + plane.y * minY + plane.z * maxZ + plane.w > 0) continue
            if (plane.x * minX + plane.y * maxY + plane.z * maxZ + plane.w > 0) continue
            if (plane.x * maxX + plane.y * maxY + plane.z * maxZ + plane.w > 0) continue
            return false
        }
        return true
    }
}
