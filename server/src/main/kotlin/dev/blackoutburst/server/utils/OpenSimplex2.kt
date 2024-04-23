package dev.blackoutburst.server.utils

object OpenSimplex2 {
    private const val PRIME_X = 0x5205402B9270C86FL
    private const val PRIME_Y = 0x598CD327003817B5L
    private const val PRIME_Z = 0x5BCC226E9FA0BACBL
    private const val PRIME_W = 0x56CC5227E58F554BL
    private const val HASH_MULTIPLIER = 0x53A3F72DEEC546F5L
    private const val SEED_FLIP_3D = -0x52D547B2E96ED629L
    private const val SEED_OFFSET_4D = 0xE83DC3E0DA7164DL

    private const val ROOT2OVER2 = 0.7071067811865476
    private const val SKEW_2D = 0.366025403784439
    private const val UNSKEW_2D = -0.21132486540518713

    private const val ROOT3OVER3 = 0.577350269189626
    private const val FALLBACK_ROTATE_3D = 2.0 / 3.0
    private const val ROTATE_3D_ORTHOGONALIZER = UNSKEW_2D

    private const val SKEW_4D = -0.138196601125011f
    private const val UNSKEW_4D = 0.309016994374947f
    private const val LATTICE_STEP_4D = 0.2f

    private const val N_GRADS_2D_EXPONENT = 7
    private const val N_GRADS_3D_EXPONENT = 8
    private const val N_GRADS_4D_EXPONENT = 9
    private const val N_GRADS_2D = 1 shl N_GRADS_2D_EXPONENT
    private const val N_GRADS_3D = 1 shl N_GRADS_3D_EXPONENT
    private const val N_GRADS_4D = 1 shl N_GRADS_4D_EXPONENT

    private const val NORMALIZER_2D = 0.01001634121365712
    private const val NORMALIZER_3D = 0.07969837668935331
    private const val NORMALIZER_4D = 0.0220065933241897

    private const val RSQUARED_2D = 0.5f
    private const val RSQUARED_3D = 0.6f
    private const val RSQUARED_4D = 0.6f


    /*
     * Noise Evaluators
     */
    /**
     * 2D Simplex noise, standard lattice orientation.
     */
    fun noise2(seed: Long, x: Double, y: Double): Float {
        // Get points for A2* lattice

        val s = SKEW_2D * (x + y)
        val xs = x + s
        val ys = y + s

        return noise2_UnskewedBase(seed, xs, ys)
    }

    /**
     * 2D Simplex noise, with Y pointing down the main diagonal.
     * Might be better for a 2D sandbox style game, where Y is vertical.
     * Probably slightly less optimal for heightmaps or continent maps,
     * unless your map is centered around an equator. It's a subtle
     * difference, but the option is here to make it an easy choice.
     */
    fun noise2_ImproveX(seed: Long, x: Double, y: Double): Float {
        // Skew transform and rotation baked into one.

        val xx = x * ROOT2OVER2
        val yy = y * (ROOT2OVER2 * (1 + 2 * SKEW_2D))

        return noise2_UnskewedBase(seed, yy + xx, yy - xx)
    }

    /**
     * 2D Simplex noise base.
     */
    private fun noise2_UnskewedBase(seed: Long, xs: Double, ys: Double): Float {
        // Get base points and offsets.

        val xsb = fastFloor(xs)
        val ysb = fastFloor(ys)
        val xi = (xs - xsb).toFloat()
        val yi = (ys - ysb).toFloat()

        // Prime pre-multiplication for hash.
        val xsbp = xsb * PRIME_X
        val ysbp = ysb * PRIME_Y

        // Unskew.
        val t = (xi + yi) * UNSKEW_2D.toFloat()
        val dx0 = xi + t
        val dy0 = yi + t

        // First vertex.
        var value = 0f
        val a0 = RSQUARED_2D - dx0 * dx0 - dy0 * dy0
        if (a0 > 0) {
            value = (a0 * a0) * (a0 * a0) * grad(seed, xsbp, ysbp, dx0, dy0)
        }

        // Second vertex.
        val a1 =
            (2 * (1 + 2 * UNSKEW_2D) * (1 / UNSKEW_2D + 2)).toFloat() * t + ((-2 * (1 + 2 * UNSKEW_2D) * (1 + 2 * UNSKEW_2D)).toFloat() + a0)
        if (a1 > 0) {
            val dx1 = dx0 - (1 + 2 * UNSKEW_2D).toFloat()
            val dy1 = dy0 - (1 + 2 * UNSKEW_2D).toFloat()
            value += (a1 * a1) * (a1 * a1) * grad(seed, xsbp + PRIME_X, ysbp + PRIME_Y, dx1, dy1)
        }

        // Third vertex.
        if (dy0 > dx0) {
            val dx2 = dx0 - UNSKEW_2D.toFloat()
            val dy2 = dy0 - (UNSKEW_2D + 1).toFloat()
            val a2 = RSQUARED_2D - dx2 * dx2 - dy2 * dy2
            if (a2 > 0) {
                value += (a2 * a2) * (a2 * a2) * grad(seed, xsbp, ysbp + PRIME_Y, dx2, dy2)
            }
        } else {
            val dx2 = dx0 - (UNSKEW_2D + 1).toFloat()
            val dy2 = dy0 - UNSKEW_2D.toFloat()
            val a2 = RSQUARED_2D - dx2 * dx2 - dy2 * dy2
            if (a2 > 0) {
                value += (a2 * a2) * (a2 * a2) * grad(seed, xsbp + PRIME_X, ysbp, dx2, dy2)
            }
        }

        return value
    }

    /**
     * 3D dev.blackoutburst.game.utils.OpenSimplex2 noise, with better visual isotropy in (X, Y).
     * Recommended for 3D terrain and time-varied animations.
     * The Z coordinate should always be the "different" coordinate in whatever your use case is.
     * If Y is vertical in world coordinates, call noise3_ImproveXZ(x, z, Y) or use noise3_XZBeforeY.
     * If Z is vertical in world coordinates, call noise3_ImproveXZ(x, y, Z).
     * For a time varied animation, call noise3_ImproveXY(x, y, T).
     */
    fun noise3_ImproveXY(seed: Long, x: Double, y: Double, z: Double): Float {
        // Re-orient the cubic lattices without skewing, so Z points up the main lattice diagonal,
        // and the planes formed by XY are moved far out of alignment with the cube faces.
        // Orthonormal rotation. Not a skew transform.

        val xy = x + y
        val s2 = xy * ROTATE_3D_ORTHOGONALIZER
        val zz = z * ROOT3OVER3
        val xr = x + s2 + zz
        val yr = y + s2 + zz
        val zr = xy * -ROOT3OVER3 + zz

        // Evaluate both lattices to form a BCC lattice.
        return noise3_UnrotatedBase(seed, xr, yr, zr)
    }

    /**
     * 3D dev.blackoutburst.game.utils.OpenSimplex2 noise, with better visual isotropy in (X, Z).
     * Recommended for 3D terrain and time-varied animations.
     * The Y coordinate should always be the "different" coordinate in whatever your use case is.
     * If Y is vertical in world coordinates, call noise3_ImproveXZ(x, Y, z).
     * If Z is vertical in world coordinates, call noise3_ImproveXZ(x, Z, y) or use noise3_ImproveXY.
     * For a time varied animation, call noise3_ImproveXZ(x, T, y) or use noise3_ImproveXY.
     */
    fun noise3_ImproveXZ(seed: Long, x: Double, y: Double, z: Double): Float {
        // Re-orient the cubic lattices without skewing, so Y points up the main lattice diagonal,
        // and the planes formed by XZ are moved far out of alignment with the cube faces.
        // Orthonormal rotation. Not a skew transform.

        val xz = x + z
        val s2 = xz * ROTATE_3D_ORTHOGONALIZER
        val yy = y * ROOT3OVER3
        val xr = x + s2 + yy
        val zr = z + s2 + yy
        val yr = xz * -ROOT3OVER3 + yy

        // Evaluate both lattices to form a BCC lattice.
        return noise3_UnrotatedBase(seed, xr, yr, zr)
    }

    /**
     * 3D dev.blackoutburst.game.utils.OpenSimplex2 noise, fallback rotation option
     * Use noise3_ImproveXY or noise3_ImproveXZ instead, wherever appropriate.
     * They have less diagonal bias. This function's best use is as a fallback.
     */
    fun noise3_Fallback(seed: Long, x: Double, y: Double, z: Double): Float {
        // Re-orient the cubic lattices via rotation, to produce a familiar look.
        // Orthonormal rotation. Not a skew transform.

        val r = FALLBACK_ROTATE_3D * (x + y + z)
        val xr = r - x
        val yr = r - y
        val zr = r - z

        // Evaluate both lattices to form a BCC lattice.
        return noise3_UnrotatedBase(seed, xr, yr, zr)
    }

    /**
     * Generate overlapping cubic lattices for 3D dev.blackoutburst.game.utils.OpenSimplex2 noise.
     */
    private fun noise3_UnrotatedBase(seed: Long, xr: Double, yr: Double, zr: Double): Float {
        // Get base points and offsets.

        var seed = seed
        val xrb = fastRound(xr)
        val yrb = fastRound(yr)
        val zrb = fastRound(zr)
        var xri = (xr - xrb).toFloat()
        var yri = (yr - yrb).toFloat()
        var zri = (zr - zrb).toFloat()

        // -1 if positive, 1 if negative.
        var xNSign = (-1.0f - xri).toInt() or 1
        var yNSign = (-1.0f - yri).toInt() or 1
        var zNSign = (-1.0f - zri).toInt() or 1

        // Compute absolute values, using the above as a shortcut. This was faster in my tests for some reason.
        var ax0 = xNSign * -xri
        var ay0 = yNSign * -yri
        var az0 = zNSign * -zri

        // Prime pre-multiplication for hash.
        var xrbp = xrb * PRIME_X
        var yrbp = yrb * PRIME_Y
        var zrbp = zrb * PRIME_Z

        // Loop: Pick an edge on each lattice copy.
        var value = 0f
        var a = (RSQUARED_3D - xri * xri) - (yri * yri + zri * zri)
        var l = 0
        while (true) {
            // Closest point on cube.
            if (a > 0) {
                value += (a * a) * (a * a) * grad(seed, xrbp, yrbp, zrbp, xri, yri, zri)
            }

            // Second-closest point.
            if (ax0 >= ay0 && ax0 >= az0) {
                var b = a + ax0 + ax0
                if (b > 1) {
                    b -= 1f
                    value += (b * b) * (b * b) * grad(seed, xrbp - xNSign * PRIME_X, yrbp, zrbp, xri + xNSign, yri, zri)
                }
            } else if (ay0 > ax0 && ay0 >= az0) {
                var b = a + ay0 + ay0
                if (b > 1) {
                    b -= 1f
                    value += (b * b) * (b * b) * grad(seed, xrbp, yrbp - yNSign * PRIME_Y, zrbp, xri, yri + yNSign, zri)
                }
            } else {
                var b = a + az0 + az0
                if (b > 1) {
                    b -= 1f
                    value += (b * b) * (b * b) * grad(seed, xrbp, yrbp, zrbp - zNSign * PRIME_Z, xri, yri, zri + zNSign)
                }
            }

            // Break from loop if we're done, skipping updates below.
            if (l == 1) break

            // Update absolute value.
            ax0 = 0.5f - ax0
            ay0 = 0.5f - ay0
            az0 = 0.5f - az0

            // Update relative coordinate.
            xri = xNSign * ax0
            yri = yNSign * ay0
            zri = zNSign * az0

            // Update falloff.
            a += (0.75f - ax0) - (ay0 + az0)

            // Update prime for hash.
            xrbp += (xNSign shr 1).toLong() and PRIME_X
            yrbp += (yNSign shr 1).toLong() and PRIME_Y
            zrbp += (zNSign shr 1).toLong() and PRIME_Z

            // Update the reverse sign indicators.
            xNSign = -xNSign
            yNSign = -yNSign
            zNSign = -zNSign

            // And finally update the seed for the other lattice copy.
            seed = seed xor SEED_FLIP_3D
            l++
        }

        return value
    }

    /**
     * 4D dev.blackoutburst.game.utils.OpenSimplex2 noise, with XYZ oriented like noise3_ImproveXY
     * and W for an extra degree of freedom. W repeats eventually.
     * Recommended for time-varied animations which texture a 3D object (W=time)
     * in a space where Z is vertical
     */
    fun noise4_ImproveXYZ_ImproveXY(seed: Long, x: Double, y: Double, z: Double, w: Double): Float {
        val xy = x + y
        val s2 = xy * -0.21132486540518699998
        val zz = z * 0.28867513459481294226
        val ww = w * 0.2236067977499788
        val xr = x + (zz + ww + s2)
        val yr = y + (zz + ww + s2)
        val zr = xy * -0.57735026918962599998 + (zz + ww)
        val wr = z * -0.866025403784439 + ww

        return noise4_UnskewedBase(seed, xr, yr, zr, wr)
    }

    /**
     * 4D dev.blackoutburst.game.utils.OpenSimplex2 noise, with XYZ oriented like noise3_ImproveXZ
     * and W for an extra degree of freedom. W repeats eventually.
     * Recommended for time-varied animations which texture a 3D object (W=time)
     * in a space where Y is vertical
     */
    fun noise4_ImproveXYZ_ImproveXZ(seed: Long, x: Double, y: Double, z: Double, w: Double): Float {
        val xz = x + z
        val s2 = xz * -0.21132486540518699998
        val yy = y * 0.28867513459481294226
        val ww = w * 0.2236067977499788
        val xr = x + (yy + ww + s2)
        val zr = z + (yy + ww + s2)
        val yr = xz * -0.57735026918962599998 + (yy + ww)
        val wr = y * -0.866025403784439 + ww

        return noise4_UnskewedBase(seed, xr, yr, zr, wr)
    }

    /**
     * 4D dev.blackoutburst.game.utils.OpenSimplex2 noise, with XYZ oriented like noise3_Fallback
     * and W for an extra degree of freedom. W repeats eventually.
     * Recommended for time-varied animations which texture a 3D object (W=time)
     * where there isn't a clear distinction between horizontal and vertical
     */
    fun noise4_ImproveXYZ(seed: Long, x: Double, y: Double, z: Double, w: Double): Float {
        val xyz = x + y + z
        val ww = w * 0.2236067977499788
        val s2 = xyz * -0.16666666666666666 + ww
        val xs = x + s2
        val ys = y + s2
        val zs = z + s2
        val ws = -0.5 * xyz + ww

        return noise4_UnskewedBase(seed, xs, ys, zs, ws)
    }

    /**
     * 4D dev.blackoutburst.game.utils.OpenSimplex2 noise, with XY and ZW forming orthogonal triangular-based planes.
     * Recommended for 3D terrain, where X and Y (or Z and W) are horizontal.
     * Recommended for noise(x, y, sin(time), cos(time)) trick.
     */
    fun noise4_ImproveXY_ImproveZW(seed: Long, x: Double, y: Double, z: Double, w: Double): Float {
        val s2 = (x + y) * -0.178275657951399372 + (z + w) * 0.215623393288842828
        val t2 = (z + w) * -0.403949762580207112 + (x + y) * -0.375199083010075342
        val xs = x + s2
        val ys = y + s2
        val zs = z + t2
        val ws = w + t2

        return noise4_UnskewedBase(seed, xs, ys, zs, ws)
    }

    /**
     * 4D dev.blackoutburst.game.utils.OpenSimplex2 noise, fallback lattice orientation.
     */
    fun noise4_Fallback(seed: Long, x: Double, y: Double, z: Double, w: Double): Float {
        // Get points for A4 lattice

        val s = SKEW_4D * (x + y + z + w)
        val xs = x + s
        val ys = y + s
        val zs = z + s
        val ws = w + s

        return noise4_UnskewedBase(seed, xs, ys, zs, ws)
    }

    /**
     * 4D dev.blackoutburst.game.utils.OpenSimplex2 noise base.
     */
    private fun noise4_UnskewedBase(seed: Long, xs: Double, ys: Double, zs: Double, ws: Double): Float {
        // Get base points and offsets

        var seed = seed
        val xsb = fastFloor(xs)
        val ysb = fastFloor(ys)
        val zsb = fastFloor(zs)
        val wsb = fastFloor(ws)
        var xsi = (xs - xsb).toFloat()
        var ysi = (ys - ysb).toFloat()
        var zsi = (zs - zsb).toFloat()
        var wsi = (ws - wsb).toFloat()

        // Determine which lattice we can be confident has a contributing point its corresponding cell's base simplex.
        // We only look at the spaces between the diagonal planes. This proved effective in all of my tests.
        val siSum = (xsi + ysi) + (zsi + wsi)
        val startingLattice = (siSum * 1.25).toInt()

        // Offset for seed based on first lattice copy.
        seed += startingLattice * SEED_OFFSET_4D

        // Offset for lattice point relative positions (skewed)
        val startingLatticeOffset = startingLattice * -LATTICE_STEP_4D
        xsi += startingLatticeOffset
        ysi += startingLatticeOffset
        zsi += startingLatticeOffset
        wsi += startingLatticeOffset

        // Prep for vertex contributions.
        var ssi = (siSum + startingLatticeOffset * 4) * UNSKEW_4D

        // Prime pre-multiplication for hash.
        var xsvp = xsb * PRIME_X
        var ysvp = ysb * PRIME_Y
        var zsvp = zsb * PRIME_Z
        var wsvp = wsb * PRIME_W

        // Five points to add, total, from five copies of the A4 lattice.
        var value = 0f
        var i = 0
        while (true) {
            // Next point is the closest vertex on the 4-simplex whose base vertex is the aforementioned vertex.
            val score0 = 1.0 + ssi * (-1.0 / UNSKEW_4D) // Seems slightly faster than 1.0-xsi-ysi-zsi-wsi
            if (xsi >= ysi && xsi >= zsi && xsi >= wsi && xsi >= score0) {
                xsvp += PRIME_X
                xsi -= 1f
                ssi -= UNSKEW_4D
            } else if (ysi > xsi && ysi >= zsi && ysi >= wsi && ysi >= score0) {
                ysvp += PRIME_Y
                ysi -= 1f
                ssi -= UNSKEW_4D
            } else if (zsi > xsi && zsi > ysi && zsi >= wsi && zsi >= score0) {
                zsvp += PRIME_Z
                zsi -= 1f
                ssi -= UNSKEW_4D
            } else if (wsi > xsi && wsi > ysi && wsi > zsi && wsi >= score0) {
                wsvp += PRIME_W
                wsi -= 1f
                ssi -= UNSKEW_4D
            }

            // gradient contribution with falloff.
            val dx = xsi + ssi
            val dy = ysi + ssi
            val dz = zsi + ssi
            val dw = wsi + ssi
            var a = (dx * dx + dy * dy) + (dz * dz + dw * dw)
            if (a < RSQUARED_4D) {
                a -= RSQUARED_4D
                a *= a
                value += a * a * grad(seed, xsvp, ysvp, zsvp, wsvp, dx, dy, dz, dw)
            }

            // Break from loop if we're done, skipping updates below.
            if (i == 4) break

            // Update for next lattice copy shifted down by <-0.2, -0.2, -0.2, -0.2>.
            xsi += LATTICE_STEP_4D
            ysi += LATTICE_STEP_4D
            zsi += LATTICE_STEP_4D
            wsi += LATTICE_STEP_4D
            ssi += LATTICE_STEP_4D * 4 * UNSKEW_4D
            seed -= SEED_OFFSET_4D

            // Because we don't always start on the same lattice copy, there's a special reset case.
            if (i == startingLattice) {
                xsvp -= PRIME_X
                ysvp -= PRIME_Y
                zsvp -= PRIME_Z
                wsvp -= PRIME_W
                seed += SEED_OFFSET_4D * 5
            }
            i++
        }

        return value
    }

    /*
     * Utility
     */
    private fun grad(seed: Long, xsvp: Long, ysvp: Long, dx: Float, dy: Float): Float {
        var hash = seed xor xsvp xor ysvp
        hash *= HASH_MULTIPLIER
        hash = hash xor (hash shr (64 - N_GRADS_2D_EXPONENT + 1))
        val gi = hash.toInt() and ((N_GRADS_2D - 1) shl 1)
        return GRADIENTS_2D[gi or 0] * dx + GRADIENTS_2D[gi or 1] * dy
    }

    private fun grad(seed: Long, xrvp: Long, yrvp: Long, zrvp: Long, dx: Float, dy: Float, dz: Float): Float {
        var hash = (seed xor xrvp) xor (yrvp xor zrvp)
        hash *= HASH_MULTIPLIER
        hash = hash xor (hash shr (64 - N_GRADS_3D_EXPONENT + 2))
        val gi = hash.toInt() and ((N_GRADS_3D - 1) shl 2)
        return GRADIENTS_3D[gi or 0] * dx + GRADIENTS_3D[gi or 1] * dy + GRADIENTS_3D[gi or 2] * dz
    }

    private fun grad(
        seed: Long,
        xsvp: Long,
        ysvp: Long,
        zsvp: Long,
        wsvp: Long,
        dx: Float,
        dy: Float,
        dz: Float,
        dw: Float
    ): Float {
        var hash = seed xor (xsvp xor ysvp) xor (zsvp xor wsvp)
        hash *= HASH_MULTIPLIER
        hash = hash xor (hash shr (64 - N_GRADS_4D_EXPONENT + 2))
        val gi = hash.toInt() and ((N_GRADS_4D - 1) shl 2)
        return (GRADIENTS_4D[gi or 0] * dx + GRADIENTS_4D[gi or 1] * dy) + (GRADIENTS_4D[gi or 2] * dz + GRADIENTS_4D[gi or 3] * dw)
    }

    private fun fastFloor(x: Double): Int {
        val xi = x.toInt()
        return if (x < xi) xi - 1 else xi
    }

    private fun fastRound(x: Double): Int {
        return if (x < 0) (x - 0.5).toInt() else (x + 0.5).toInt()
    }

    /*
     * gradients
     */
    private var GRADIENTS_2D = FloatArray(N_GRADS_2D * 2)
    private var GRADIENTS_3D: FloatArray
    private var GRADIENTS_4D: FloatArray

    init {
        val grad2 = floatArrayOf(
            0.38268343236509f, 0.923879532511287f,
            0.923879532511287f, 0.38268343236509f,
            0.923879532511287f, -0.38268343236509f,
            0.38268343236509f, -0.923879532511287f,
            -0.38268343236509f, -0.923879532511287f,
            -0.923879532511287f, -0.38268343236509f,
            -0.923879532511287f, 0.38268343236509f,
            -0.38268343236509f, 0.923879532511287f,  //-------------------------------------//
            0.130526192220052f, 0.99144486137381f,
            0.608761429008721f, 0.793353340291235f,
            0.793353340291235f, 0.608761429008721f,
            0.99144486137381f, 0.130526192220051f,
            0.99144486137381f, -0.130526192220051f,
            0.793353340291235f, -0.60876142900872f,
            0.608761429008721f, -0.793353340291235f,
            0.130526192220052f, -0.99144486137381f,
            -0.130526192220052f, -0.99144486137381f,
            -0.608761429008721f, -0.793353340291235f,
            -0.793353340291235f, -0.608761429008721f,
            -0.99144486137381f, -0.130526192220052f,
            -0.99144486137381f, 0.130526192220051f,
            -0.793353340291235f, 0.608761429008721f,
            -0.608761429008721f, 0.793353340291235f,
            -0.130526192220052f, 0.99144486137381f,
        )
        for (i in grad2.indices) {
            grad2[i] = (grad2[i] / NORMALIZER_2D).toFloat()
        }
        run {
            var i = 0
            var j = 0
            while (i < GRADIENTS_2D.size) {
                if (j == grad2.size) j = 0
                GRADIENTS_2D[i] = grad2[j]
                i++
                j++
            }
        }

        GRADIENTS_3D = FloatArray(N_GRADS_3D * 4)
        val grad3 = floatArrayOf(
            2.22474487139f,
            2.22474487139f,
            -1.0f,
            0.0f,
            2.22474487139f,
            2.22474487139f,
            1.0f,
            0.0f,
            3.0862664687972017f,
            1.1721513422464978f,
            0.0f,
            0.0f,
            1.1721513422464978f,
            3.0862664687972017f,
            0.0f,
            0.0f,
            -2.22474487139f,
            2.22474487139f,
            -1.0f,
            0.0f,
            -2.22474487139f,
            2.22474487139f,
            1.0f,
            0.0f,
            -1.1721513422464978f,
            3.0862664687972017f,
            0.0f,
            0.0f,
            -3.0862664687972017f,
            1.1721513422464978f,
            0.0f,
            0.0f,
            -1.0f,
            -2.22474487139f,
            -2.22474487139f,
            0.0f,
            1.0f,
            -2.22474487139f,
            -2.22474487139f,
            0.0f,
            0.0f,
            -3.0862664687972017f,
            -1.1721513422464978f,
            0.0f,
            0.0f,
            -1.1721513422464978f,
            -3.0862664687972017f,
            0.0f,
            -1.0f,
            -2.22474487139f,
            2.22474487139f,
            0.0f,
            1.0f,
            -2.22474487139f,
            2.22474487139f,
            0.0f,
            0.0f,
            -1.1721513422464978f,
            3.0862664687972017f,
            0.0f,
            0.0f,
            -3.0862664687972017f,
            1.1721513422464978f,
            0.0f,  //--------------------------------------------------------------------//
            -2.22474487139f,
            -2.22474487139f,
            -1.0f,
            0.0f,
            -2.22474487139f,
            -2.22474487139f,
            1.0f,
            0.0f,
            -3.0862664687972017f,
            -1.1721513422464978f,
            0.0f,
            0.0f,
            -1.1721513422464978f,
            -3.0862664687972017f,
            0.0f,
            0.0f,
            -2.22474487139f,
            -1.0f,
            -2.22474487139f,
            0.0f,
            -2.22474487139f,
            1.0f,
            -2.22474487139f,
            0.0f,
            -1.1721513422464978f,
            0.0f,
            -3.0862664687972017f,
            0.0f,
            -3.0862664687972017f,
            0.0f,
            -1.1721513422464978f,
            0.0f,
            -2.22474487139f,
            -1.0f,
            2.22474487139f,
            0.0f,
            -2.22474487139f,
            1.0f,
            2.22474487139f,
            0.0f,
            -3.0862664687972017f,
            0.0f,
            1.1721513422464978f,
            0.0f,
            -1.1721513422464978f,
            0.0f,
            3.0862664687972017f,
            0.0f,
            -1.0f,
            2.22474487139f,
            -2.22474487139f,
            0.0f,
            1.0f,
            2.22474487139f,
            -2.22474487139f,
            0.0f,
            0.0f,
            1.1721513422464978f,
            -3.0862664687972017f,
            0.0f,
            0.0f,
            3.0862664687972017f,
            -1.1721513422464978f,
            0.0f,
            -1.0f,
            2.22474487139f,
            2.22474487139f,
            0.0f,
            1.0f,
            2.22474487139f,
            2.22474487139f,
            0.0f,
            0.0f,
            3.0862664687972017f,
            1.1721513422464978f,
            0.0f,
            0.0f,
            1.1721513422464978f,
            3.0862664687972017f,
            0.0f,
            2.22474487139f,
            -2.22474487139f,
            -1.0f,
            0.0f,
            2.22474487139f,
            -2.22474487139f,
            1.0f,
            0.0f,
            1.1721513422464978f,
            -3.0862664687972017f,
            0.0f,
            0.0f,
            3.0862664687972017f,
            -1.1721513422464978f,
            0.0f,
            0.0f,
            2.22474487139f,
            -1.0f,
            -2.22474487139f,
            0.0f,
            2.22474487139f,
            1.0f,
            -2.22474487139f,
            0.0f,
            3.0862664687972017f,
            0.0f,
            -1.1721513422464978f,
            0.0f,
            1.1721513422464978f,
            0.0f,
            -3.0862664687972017f,
            0.0f,
            2.22474487139f,
            -1.0f,
            2.22474487139f,
            0.0f,
            2.22474487139f,
            1.0f,
            2.22474487139f,
            0.0f,
            1.1721513422464978f,
            0.0f,
            3.0862664687972017f,
            0.0f,
            3.0862664687972017f,
            0.0f,
            1.1721513422464978f,
            0.0f,
        )
        for (i in grad3.indices) {
            grad3[i] = (grad3[i] / NORMALIZER_3D).toFloat()
        }
        run {
            var i = 0
            var j = 0
            while (i < GRADIENTS_3D.size) {
                if (j == grad3.size) j = 0
                GRADIENTS_3D[i] = grad3[j]
                i++
                j++
            }
        }

        GRADIENTS_4D = FloatArray(N_GRADS_4D * 4)
        val grad4 = floatArrayOf(
            -0.6740059517812944f,
            -0.3239847771997537f,
            -0.3239847771997537f,
            0.5794684678643381f,
            -0.7504883828755602f,
            -0.4004672082940195f,
            0.15296486218853164f,
            0.5029860367700724f,
            -0.7504883828755602f,
            0.15296486218853164f,
            -0.4004672082940195f,
            0.5029860367700724f,
            -0.8828161875373585f,
            0.08164729285680945f,
            0.08164729285680945f,
            0.4553054119602712f,
            -0.4553054119602712f,
            -0.08164729285680945f,
            -0.08164729285680945f,
            0.8828161875373585f,
            -0.5029860367700724f,
            -0.15296486218853164f,
            0.4004672082940195f,
            0.7504883828755602f,
            -0.5029860367700724f,
            0.4004672082940195f,
            -0.15296486218853164f,
            0.7504883828755602f,
            -0.5794684678643381f,
            0.3239847771997537f,
            0.3239847771997537f,
            0.6740059517812944f,
            -0.6740059517812944f,
            -0.3239847771997537f,
            0.5794684678643381f,
            -0.3239847771997537f,
            -0.7504883828755602f,
            -0.4004672082940195f,
            0.5029860367700724f,
            0.15296486218853164f,
            -0.7504883828755602f,
            0.15296486218853164f,
            0.5029860367700724f,
            -0.4004672082940195f,
            -0.8828161875373585f,
            0.08164729285680945f,
            0.4553054119602712f,
            0.08164729285680945f,
            -0.4553054119602712f,
            -0.08164729285680945f,
            0.8828161875373585f,
            -0.08164729285680945f,
            -0.5029860367700724f,
            -0.15296486218853164f,
            0.7504883828755602f,
            0.4004672082940195f,
            -0.5029860367700724f,
            0.4004672082940195f,
            0.7504883828755602f,
            -0.15296486218853164f,
            -0.5794684678643381f,
            0.3239847771997537f,
            0.6740059517812944f,
            0.3239847771997537f,
            -0.6740059517812944f,
            0.5794684678643381f,
            -0.3239847771997537f,
            -0.3239847771997537f,
            -0.7504883828755602f,
            0.5029860367700724f,
            -0.4004672082940195f,
            0.15296486218853164f,
            -0.7504883828755602f,
            0.5029860367700724f,
            0.15296486218853164f,
            -0.4004672082940195f,
            -0.8828161875373585f,
            0.4553054119602712f,
            0.08164729285680945f,
            0.08164729285680945f,
            -0.4553054119602712f,
            0.8828161875373585f,
            -0.08164729285680945f,
            -0.08164729285680945f,
            -0.5029860367700724f,
            0.7504883828755602f,
            -0.15296486218853164f,
            0.4004672082940195f,
            -0.5029860367700724f,
            0.7504883828755602f,
            0.4004672082940195f,
            -0.15296486218853164f,
            -0.5794684678643381f,
            0.6740059517812944f,
            0.3239847771997537f,
            0.3239847771997537f,
            0.5794684678643381f,
            -0.6740059517812944f,
            -0.3239847771997537f,
            -0.3239847771997537f,
            0.5029860367700724f,
            -0.7504883828755602f,
            -0.4004672082940195f,
            0.15296486218853164f,
            0.5029860367700724f,
            -0.7504883828755602f,
            0.15296486218853164f,
            -0.4004672082940195f,
            0.4553054119602712f,
            -0.8828161875373585f,
            0.08164729285680945f,
            0.08164729285680945f,
            0.8828161875373585f,
            -0.4553054119602712f,
            -0.08164729285680945f,
            -0.08164729285680945f,
            0.7504883828755602f,
            -0.5029860367700724f,
            -0.15296486218853164f,
            0.4004672082940195f,
            0.7504883828755602f,
            -0.5029860367700724f,
            0.4004672082940195f,
            -0.15296486218853164f,
            0.6740059517812944f,
            -0.5794684678643381f,
            0.3239847771997537f,
            0.3239847771997537f,  //------------------------------------------------------------------------------------------//
            -0.753341017856078f,
            -0.37968289875261624f,
            -0.37968289875261624f,
            -0.37968289875261624f,
            -0.7821684431180708f,
            -0.4321472685365301f,
            -0.4321472685365301f,
            0.12128480194602098f,
            -0.7821684431180708f,
            -0.4321472685365301f,
            0.12128480194602098f,
            -0.4321472685365301f,
            -0.7821684431180708f,
            0.12128480194602098f,
            -0.4321472685365301f,
            -0.4321472685365301f,
            -0.8586508742123365f,
            -0.508629699630796f,
            0.044802370851755174f,
            0.044802370851755174f,
            -0.8586508742123365f,
            0.044802370851755174f,
            -0.508629699630796f,
            0.044802370851755174f,
            -0.8586508742123365f,
            0.044802370851755174f,
            0.044802370851755174f,
            -0.508629699630796f,
            -0.9982828964265062f,
            -0.03381941603233842f,
            -0.03381941603233842f,
            -0.03381941603233842f,
            -0.37968289875261624f,
            -0.753341017856078f,
            -0.37968289875261624f,
            -0.37968289875261624f,
            -0.4321472685365301f,
            -0.7821684431180708f,
            -0.4321472685365301f,
            0.12128480194602098f,
            -0.4321472685365301f,
            -0.7821684431180708f,
            0.12128480194602098f,
            -0.4321472685365301f,
            0.12128480194602098f,
            -0.7821684431180708f,
            -0.4321472685365301f,
            -0.4321472685365301f,
            -0.508629699630796f,
            -0.8586508742123365f,
            0.044802370851755174f,
            0.044802370851755174f,
            0.044802370851755174f,
            -0.8586508742123365f,
            -0.508629699630796f,
            0.044802370851755174f,
            0.044802370851755174f,
            -0.8586508742123365f,
            0.044802370851755174f,
            -0.508629699630796f,
            -0.03381941603233842f,
            -0.9982828964265062f,
            -0.03381941603233842f,
            -0.03381941603233842f,
            -0.37968289875261624f,
            -0.37968289875261624f,
            -0.753341017856078f,
            -0.37968289875261624f,
            -0.4321472685365301f,
            -0.4321472685365301f,
            -0.7821684431180708f,
            0.12128480194602098f,
            -0.4321472685365301f,
            0.12128480194602098f,
            -0.7821684431180708f,
            -0.4321472685365301f,
            0.12128480194602098f,
            -0.4321472685365301f,
            -0.7821684431180708f,
            -0.4321472685365301f,
            -0.508629699630796f,
            0.044802370851755174f,
            -0.8586508742123365f,
            0.044802370851755174f,
            0.044802370851755174f,
            -0.508629699630796f,
            -0.8586508742123365f,
            0.044802370851755174f,
            0.044802370851755174f,
            0.044802370851755174f,
            -0.8586508742123365f,
            -0.508629699630796f,
            -0.03381941603233842f,
            -0.03381941603233842f,
            -0.9982828964265062f,
            -0.03381941603233842f,
            -0.37968289875261624f,
            -0.37968289875261624f,
            -0.37968289875261624f,
            -0.753341017856078f,
            -0.4321472685365301f,
            -0.4321472685365301f,
            0.12128480194602098f,
            -0.7821684431180708f,
            -0.4321472685365301f,
            0.12128480194602098f,
            -0.4321472685365301f,
            -0.7821684431180708f,
            0.12128480194602098f,
            -0.4321472685365301f,
            -0.4321472685365301f,
            -0.7821684431180708f,
            -0.508629699630796f,
            0.044802370851755174f,
            0.044802370851755174f,
            -0.8586508742123365f,
            0.044802370851755174f,
            -0.508629699630796f,
            0.044802370851755174f,
            -0.8586508742123365f,
            0.044802370851755174f,
            0.044802370851755174f,
            -0.508629699630796f,
            -0.8586508742123365f,
            -0.03381941603233842f,
            -0.03381941603233842f,
            -0.03381941603233842f,
            -0.9982828964265062f,
            -0.3239847771997537f,
            -0.6740059517812944f,
            -0.3239847771997537f,
            0.5794684678643381f,
            -0.4004672082940195f,
            -0.7504883828755602f,
            0.15296486218853164f,
            0.5029860367700724f,
            0.15296486218853164f,
            -0.7504883828755602f,
            -0.4004672082940195f,
            0.5029860367700724f,
            0.08164729285680945f,
            -0.8828161875373585f,
            0.08164729285680945f,
            0.4553054119602712f,
            -0.08164729285680945f,
            -0.4553054119602712f,
            -0.08164729285680945f,
            0.8828161875373585f,
            -0.15296486218853164f,
            -0.5029860367700724f,
            0.4004672082940195f,
            0.7504883828755602f,
            0.4004672082940195f,
            -0.5029860367700724f,
            -0.15296486218853164f,
            0.7504883828755602f,
            0.3239847771997537f,
            -0.5794684678643381f,
            0.3239847771997537f,
            0.6740059517812944f,
            -0.3239847771997537f,
            -0.3239847771997537f,
            -0.6740059517812944f,
            0.5794684678643381f,
            -0.4004672082940195f,
            0.15296486218853164f,
            -0.7504883828755602f,
            0.5029860367700724f,
            0.15296486218853164f,
            -0.4004672082940195f,
            -0.7504883828755602f,
            0.5029860367700724f,
            0.08164729285680945f,
            0.08164729285680945f,
            -0.8828161875373585f,
            0.4553054119602712f,
            -0.08164729285680945f,
            -0.08164729285680945f,
            -0.4553054119602712f,
            0.8828161875373585f,
            -0.15296486218853164f,
            0.4004672082940195f,
            -0.5029860367700724f,
            0.7504883828755602f,
            0.4004672082940195f,
            -0.15296486218853164f,
            -0.5029860367700724f,
            0.7504883828755602f,
            0.3239847771997537f,
            0.3239847771997537f,
            -0.5794684678643381f,
            0.6740059517812944f,
            -0.3239847771997537f,
            -0.6740059517812944f,
            0.5794684678643381f,
            -0.3239847771997537f,
            -0.4004672082940195f,
            -0.7504883828755602f,
            0.5029860367700724f,
            0.15296486218853164f,
            0.15296486218853164f,
            -0.7504883828755602f,
            0.5029860367700724f,
            -0.4004672082940195f,
            0.08164729285680945f,
            -0.8828161875373585f,
            0.4553054119602712f,
            0.08164729285680945f,
            -0.08164729285680945f,
            -0.4553054119602712f,
            0.8828161875373585f,
            -0.08164729285680945f,
            -0.15296486218853164f,
            -0.5029860367700724f,
            0.7504883828755602f,
            0.4004672082940195f,
            0.4004672082940195f,
            -0.5029860367700724f,
            0.7504883828755602f,
            -0.15296486218853164f,
            0.3239847771997537f,
            -0.5794684678643381f,
            0.6740059517812944f,
            0.3239847771997537f,
            -0.3239847771997537f,
            -0.3239847771997537f,
            0.5794684678643381f,
            -0.6740059517812944f,
            -0.4004672082940195f,
            0.15296486218853164f,
            0.5029860367700724f,
            -0.7504883828755602f,
            0.15296486218853164f,
            -0.4004672082940195f,
            0.5029860367700724f,
            -0.7504883828755602f,
            0.08164729285680945f,
            0.08164729285680945f,
            0.4553054119602712f,
            -0.8828161875373585f,
            -0.08164729285680945f,
            -0.08164729285680945f,
            0.8828161875373585f,
            -0.4553054119602712f,
            -0.15296486218853164f,
            0.4004672082940195f,
            0.7504883828755602f,
            -0.5029860367700724f,
            0.4004672082940195f,
            -0.15296486218853164f,
            0.7504883828755602f,
            -0.5029860367700724f,
            0.3239847771997537f,
            0.3239847771997537f,
            0.6740059517812944f,
            -0.5794684678643381f,
            -0.3239847771997537f,
            0.5794684678643381f,
            -0.6740059517812944f,
            -0.3239847771997537f,
            -0.4004672082940195f,
            0.5029860367700724f,
            -0.7504883828755602f,
            0.15296486218853164f,
            0.15296486218853164f,
            0.5029860367700724f,
            -0.7504883828755602f,
            -0.4004672082940195f,
            0.08164729285680945f,
            0.4553054119602712f,
            -0.8828161875373585f,
            0.08164729285680945f,
            -0.08164729285680945f,
            0.8828161875373585f,
            -0.4553054119602712f,
            -0.08164729285680945f,
            -0.15296486218853164f,
            0.7504883828755602f,
            -0.5029860367700724f,
            0.4004672082940195f,
            0.4004672082940195f,
            0.7504883828755602f,
            -0.5029860367700724f,
            -0.15296486218853164f,
            0.3239847771997537f,
            0.6740059517812944f,
            -0.5794684678643381f,
            0.3239847771997537f,
            -0.3239847771997537f,
            0.5794684678643381f,
            -0.3239847771997537f,
            -0.6740059517812944f,
            -0.4004672082940195f,
            0.5029860367700724f,
            0.15296486218853164f,
            -0.7504883828755602f,
            0.15296486218853164f,
            0.5029860367700724f,
            -0.4004672082940195f,
            -0.7504883828755602f,
            0.08164729285680945f,
            0.4553054119602712f,
            0.08164729285680945f,
            -0.8828161875373585f,
            -0.08164729285680945f,
            0.8828161875373585f,
            -0.08164729285680945f,
            -0.4553054119602712f,
            -0.15296486218853164f,
            0.7504883828755602f,
            0.4004672082940195f,
            -0.5029860367700724f,
            0.4004672082940195f,
            0.7504883828755602f,
            -0.15296486218853164f,
            -0.5029860367700724f,
            0.3239847771997537f,
            0.6740059517812944f,
            0.3239847771997537f,
            -0.5794684678643381f,
            0.5794684678643381f,
            -0.3239847771997537f,
            -0.6740059517812944f,
            -0.3239847771997537f,
            0.5029860367700724f,
            -0.4004672082940195f,
            -0.7504883828755602f,
            0.15296486218853164f,
            0.5029860367700724f,
            0.15296486218853164f,
            -0.7504883828755602f,
            -0.4004672082940195f,
            0.4553054119602712f,
            0.08164729285680945f,
            -0.8828161875373585f,
            0.08164729285680945f,
            0.8828161875373585f,
            -0.08164729285680945f,
            -0.4553054119602712f,
            -0.08164729285680945f,
            0.7504883828755602f,
            -0.15296486218853164f,
            -0.5029860367700724f,
            0.4004672082940195f,
            0.7504883828755602f,
            0.4004672082940195f,
            -0.5029860367700724f,
            -0.15296486218853164f,
            0.6740059517812944f,
            0.3239847771997537f,
            -0.5794684678643381f,
            0.3239847771997537f,
            0.5794684678643381f,
            -0.3239847771997537f,
            -0.3239847771997537f,
            -0.6740059517812944f,
            0.5029860367700724f,
            -0.4004672082940195f,
            0.15296486218853164f,
            -0.7504883828755602f,
            0.5029860367700724f,
            0.15296486218853164f,
            -0.4004672082940195f,
            -0.7504883828755602f,
            0.4553054119602712f,
            0.08164729285680945f,
            0.08164729285680945f,
            -0.8828161875373585f,
            0.8828161875373585f,
            -0.08164729285680945f,
            -0.08164729285680945f,
            -0.4553054119602712f,
            0.7504883828755602f,
            -0.15296486218853164f,
            0.4004672082940195f,
            -0.5029860367700724f,
            0.7504883828755602f,
            0.4004672082940195f,
            -0.15296486218853164f,
            -0.5029860367700724f,
            0.6740059517812944f,
            0.3239847771997537f,
            0.3239847771997537f,
            -0.5794684678643381f,
            0.03381941603233842f,
            0.03381941603233842f,
            0.03381941603233842f,
            0.9982828964265062f,
            -0.044802370851755174f,
            -0.044802370851755174f,
            0.508629699630796f,
            0.8586508742123365f,
            -0.044802370851755174f,
            0.508629699630796f,
            -0.044802370851755174f,
            0.8586508742123365f,
            -0.12128480194602098f,
            0.4321472685365301f,
            0.4321472685365301f,
            0.7821684431180708f,
            0.508629699630796f,
            -0.044802370851755174f,
            -0.044802370851755174f,
            0.8586508742123365f,
            0.4321472685365301f,
            -0.12128480194602098f,
            0.4321472685365301f,
            0.7821684431180708f,
            0.4321472685365301f,
            0.4321472685365301f,
            -0.12128480194602098f,
            0.7821684431180708f,
            0.37968289875261624f,
            0.37968289875261624f,
            0.37968289875261624f,
            0.753341017856078f,
            0.03381941603233842f,
            0.03381941603233842f,
            0.9982828964265062f,
            0.03381941603233842f,
            -0.044802370851755174f,
            0.044802370851755174f,
            0.8586508742123365f,
            0.508629699630796f,
            -0.044802370851755174f,
            0.508629699630796f,
            0.8586508742123365f,
            -0.044802370851755174f,
            -0.12128480194602098f,
            0.4321472685365301f,
            0.7821684431180708f,
            0.4321472685365301f,
            0.508629699630796f,
            -0.044802370851755174f,
            0.8586508742123365f,
            -0.044802370851755174f,
            0.4321472685365301f,
            -0.12128480194602098f,
            0.7821684431180708f,
            0.4321472685365301f,
            0.4321472685365301f,
            0.4321472685365301f,
            0.7821684431180708f,
            -0.12128480194602098f,
            0.37968289875261624f,
            0.37968289875261624f,
            0.753341017856078f,
            0.37968289875261624f,
            0.03381941603233842f,
            0.9982828964265062f,
            0.03381941603233842f,
            0.03381941603233842f,
            -0.044802370851755174f,
            0.8586508742123365f,
            -0.044802370851755174f,
            0.508629699630796f,
            -0.044802370851755174f,
            0.8586508742123365f,
            0.508629699630796f,
            -0.044802370851755174f,
            -0.12128480194602098f,
            0.7821684431180708f,
            0.4321472685365301f,
            0.4321472685365301f,
            0.508629699630796f,
            0.8586508742123365f,
            -0.044802370851755174f,
            -0.044802370851755174f,
            0.4321472685365301f,
            0.7821684431180708f,
            -0.12128480194602098f,
            0.4321472685365301f,
            0.4321472685365301f,
            0.7821684431180708f,
            0.4321472685365301f,
            -0.12128480194602098f,
            0.37968289875261624f,
            0.753341017856078f,
            0.37968289875261624f,
            0.37968289875261624f,
            0.9982828964265062f,
            0.03381941603233842f,
            0.03381941603233842f,
            0.03381941603233842f,
            0.8586508742123365f,
            -0.044802370851755174f,
            -0.044802370851755174f,
            0.508629699630796f,
            0.8586508742123365f,
            -0.044802370851755174f,
            0.508629699630796f,
            -0.044802370851755174f,
            0.7821684431180708f,
            -0.12128480194602098f,
            0.4321472685365301f,
            0.4321472685365301f,
            0.8586508742123365f,
            0.508629699630796f,
            -0.044802370851755174f,
            -0.044802370851755174f,
            0.7821684431180708f,
            0.4321472685365301f,
            -0.12128480194602098f,
            0.4321472685365301f,
            0.7821684431180708f,
            0.4321472685365301f,
            0.4321472685365301f,
            -0.12128480194602098f,
            0.753341017856078f,
            0.37968289875261624f,
            0.37968289875261624f,
            0.37968289875261624f,
        )
        for (i in grad4.indices) {
            grad4[i] = (grad4[i] / NORMALIZER_4D).toFloat()
        }
        var i = 0
        var j = 0
        while (i < GRADIENTS_4D.size) {
            if (j == grad4.size) j = 0
            GRADIENTS_4D[i] = grad4[j]
            i++
            j++
        }
    }
}