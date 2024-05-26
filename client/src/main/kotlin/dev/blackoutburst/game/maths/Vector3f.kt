package dev.blackoutburst.game.maths

import kotlin.math.round
import kotlin.math.sqrt

class Vector3f {
    var x: Float
    var y: Float
    var z: Float

    constructor() {
        this.x = 0.0f
        this.y = 0.0f
        this.z = 0.0f
    }

    constructor(size: Float) {
        this.x = size
        this.y = size
        this.z = size
    }

    constructor(x: Float, y: Float, z: Float) {
        this.x = x
        this.y = y
        this.z = z
    }

    fun set(x: Float, y: Float, z: Float) {
        this.x = x
        this.y = y
        this.z = z
    }

    operator fun div(scalar: Float) = Vector3f(x / scalar, y / scalar, z / scalar)
    operator fun minus(other: Vector3f) = Vector3f(x - other.x, y - other.y, z - other.z)
    operator fun plus(other: Vector3f) = Vector3f(x + other.x, y + other.y, z + other.z)
    operator fun plus(value: Float): Vector3f = Vector3f(this.x + value, this.y + value, this.z + value)
    operator fun times(scalar: Float) = Vector3f(x * scalar, y * scalar, z * scalar)
    operator fun times(other: Vector3f): Vector3f = Vector3f(this.x * other.x, this.y * other.y, this.z * other.z)

    fun normalize(): Vector3f {
        val mag = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        if (mag != 0f) {
            x /= mag
            y /= mag
            z /= mag
        }

        return (this)
    }

    fun mul(v: Float): Vector3f {
        x *= v
        y *= v
        z *= v

        return (this)
    }

    fun length(): Float {
        return (sqrt((x * x + y * y + z * z).toDouble()).toFloat())
    }

    fun toInt(): Vector3i = Vector3i(round(this.x).toInt(), round(this.y).toInt(), round(this.z).toInt())

    fun copy(): Vector3f {
        val newVector = Vector3f()
        newVector.x = this.x
        newVector.y = this.y
        newVector.z = this.z

        return (newVector)
    }

    fun lerp(target: Vector3f, alpha: Float): Vector3f {
        val newX = (1 - alpha) * x + alpha * target.x
        val newY = (1 - alpha) * y + alpha * target.y
        val newZ = (1 - alpha) * z + alpha * target.z
        return Vector3f(newX, newY, newZ)
    }

    override fun toString(): String {
        return "[$x, $y, $z]"
    }
}
