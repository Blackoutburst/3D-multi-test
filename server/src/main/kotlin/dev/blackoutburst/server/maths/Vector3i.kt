package dev.blackoutburst.server.maths

import kotlin.math.sqrt

class Vector3i {
    var x: Int
    var y: Int
    var z: Int

    constructor() {
        this.x = 0
        this.y = 0
        this.z = 0
    }

    constructor(size: Int) {
        this.x = size
        this.y = size
        this.z = size
    }

    constructor(x: Int, y: Int, z: Int) {
        this.x = x
        this.y = y
        this.z = z
    }

    fun set(x: Int, y: Int, z: Int) {
        this.x = x
        this.y = y
        this.z = z
    }

    fun distance(pos: Vector3i): Int = (pos.x - this.x) * (pos.x - this.x) + (pos.y - this.y) * (pos.y - this.y) + (pos.z - this.z) * (pos.z - this.z)

    fun normalize(): Vector3i {
        val mag = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        if (mag != 0f) {
            x = (x / mag).toInt()
            y = (y / mag).toInt()
            z = (z / mag).toInt()
        }

        return (this)
    }

    fun mul(v: Float): Vector3i {
        x = (x * v).toInt()
        y = (y * v).toInt()
        z = (z * v).toInt()

        return (this)
    }

    fun length(): Float {
        return (sqrt((x * x + y * y + z * z).toDouble()).toFloat())
    }

    fun copy(): Vector3i {
        val newVector = Vector3i()
        newVector.x = this.x
        newVector.y = this.y
        newVector.z = this.z

        return (newVector)
    }

    override fun toString(): String {
        return "[$x, $y, $z]"
    }


    fun distanceTo(other: Vector3i): Int {
        val dx = (other.x - this.x)
        val dy = (other.y - this.y)
        val dz = (other.z - this.z)
        return dx * dx + dy * dy + dz * dz
    }

    operator fun plus(other: Vector3i) = Vector3i(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Vector3i) = Vector3i(x - other.x, y - other.y, z - other.z)
    operator fun div(scalar: Int) = Vector3i(x / scalar, y / scalar, z / scalar)
    operator fun times(scalar: Int) = Vector3i(x * scalar, y * scalar, z * scalar)
}
