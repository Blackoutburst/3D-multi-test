package dev.blackoutburst.game.graphics


class Color {
    var r: Float
    var g: Float
    var b: Float
    var a: Float

    constructor() {
        this.r = 0.0f
        this.g = 0.0f
        this.b = 0.0f
        this.a = 1.0f
    }

    constructor(c: Float) {
        this.r = c
        this.g = c
        this.b = c
        this.a = 1.0f
    }

    constructor(r: Float, g: Float, b: Float) {
        this.r = r
        this.g = g
        this.b = b
        this.a = 1.0f
    }

    constructor(r: Float, g: Float, b: Float, a: Float) {
        this.r = r
        this.g = g
        this.b = b
        this.a = a
    }

    fun darker(): Color {
        r -= 0.1f
        g -= 0.1f
        b -= 0.1f

        if (r < 0.0f) r = 0.0f
        if (g < 0.0f) g = 0.0f
        if (b < 0.0f) b = 0.0f

        return (this)
    }

    fun lighter(): Color {
        r += 0.1f
        g += 0.1f
        b += 0.1f

        if (r > 1.0f) r = 1.0f
        if (g > 1.0f) g = 1.0f
        if (b > 1.0f) b = 1.0f

        return (this)
    }

    fun add(c: Color): Color {
        r += c.r
        g += c.g
        b += c.b

        if (r > 1.0f) r = 1.0f
        if (g > 1.0f) g = 1.0f
        if (b > 1.0f) b = 1.0f

        return (this)
    }

    fun sub(c: Color): Color {
        r -= c.r
        g -= c.g
        b -= c.b

        if (r < 0.0f) r = 0.0f
        if (g < 0.0f) g = 0.0f
        if (b < 0.0f) b = 0.0f

        return (this)
    }

    fun mul(c: Color): Color {
        r *= c.r
        g *= c.g
        b *= c.b

        if (r < 0.0f) r = 0.0f
        if (g < 0.0f) g = 0.0f
        if (b < 0.0f) b = 0.0f
        if (r > 1.0f) r = 1.0f
        if (g > 1.0f) g = 1.0f
        if (b > 1.0f) b = 1.0f

        return (this)
    }

    fun div(c: Color): Color {
        if (c.r != 0f) r /= c.r
        if (c.g != 0f) g /= c.g
        if (c.b != 0f) b /= c.b

        if (r < 0.0f) r = 0.0f
        if (g < 0.0f) g = 0.0f
        if (b < 0.0f) b = 0.0f
        if (r > 1.0f) r = 1.0f
        if (g > 1.0f) g = 1.0f
        if (b > 1.0f) b = 1.0f

        return (this)
    }

    companion object {
        val WHITE: Color = Color(1.0f)
        val BLACK: Color = Color(0.0f)
        val GRAY: Color = Color(0.5f)
        val LIGHT_GRAY: Color = Color(0.75f)
        val DARK_GRAY: Color = Color(0.25f)
        val RED: Color = Color(1.0f, 0.0f, 0.0f)
        val GREEN: Color = Color(0.0f, 1.0f, 0.0f)
        val BLUE: Color = Color(0.0f, 0.0f, 1.0f)
        val YELLOW: Color = Color(1.0f, 1.0f, 0.0f)
        val MAGENTA: Color = Color(1.0f, 0.0f, 1.0f)
        val CYAN: Color = Color(0.0f, 1.0f, 1.0f)
        val ORANGE: Color = Color(1.0f, 0.5f, 0.0f)
        val LIGHT_BLUE: Color = Color(0.0f, 0.5f, 1.0f)
        val TRANSPARENT: Color = Color(0.0f, 0.0f, 0.0f, 0.0f)
        val PURPLE: Color = Color(0.5f, 0.0f, 1.0f, 1.0f)
        val BOGEL: Color = Color(0.25f, 0.0f, 0.27f, 1.0f)
    }
}
