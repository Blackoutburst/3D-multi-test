package dev.blackoutburst.game.maths

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
}