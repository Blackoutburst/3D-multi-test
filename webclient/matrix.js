export class Matrix {
    m00 = 1;m01 = 0;m02 = 0;m03 = 0
    m10 = 0;m11 = 1;m12 = 0;m13 = 0
    m20 = 0;m21 = 0;m22 = 1;m23 = 0
    m30 = 0;m31 = 0;m32 = 0;m33 = 1
    
    identity() {
        this.m00 = 1; this.m01 = 0; this.m02 = 0; this.m03 = 0
        this.m10 = 0; this.m11 = 1; this.m12 = 0; this.m13 = 0
        this.m20 = 0; this.m21 = 0; this.m22 = 1; this.m23 = 0
        this.m30 = 0; this.m31 = 0; this.m32 = 0; this.m33 = 1
        
        return this
    }
    
    projection(gl, fov, zNear, zFar) {
        const aspectRatio = gl.canvas.clientWidth / gl.canvas.clientHeight
        const yScale = ((1 / Math.tan(fov / 2)) * aspectRatio)
        const xScale = yScale / aspectRatio
        const frustsumLength = zFar - zNear
        
        this.m00 = xScale
        this.m11 = yScale
        this.m22 = -((zFar + zNear) / frustsumLength)
        this.m23 = -1
        this.m32 = -((2 * zNear * zFar) / frustsumLength)
        this.m33 = 0
        
        return this
    }
    
    load(src, dest) {
        let d = dest ?? new Matrix()
        
        d.m00 = src.m00; d.m01 = src.m01; d.m02 = src.m02; d.m03 = src.m03
        d.m10 = src.m10; d.m11 = src.m11; d.m12 = src.m12; d.m13 = src.m13
        d.m20 = src.m20; d.m21 = src.m21; d.m22 = src.m22; d.m23 = src.m23
        d.m30 = src.m30; d.m31 = src.m31; d.m32 = src.m32; d.m33 = src.m33
    
        return d
    }    
    
    values() {
        return [
            this.m00, this.m01, this.m02, this.m03,
            this.m10, this.m11, this.m12, this.m13,
            this.m20, this.m21, this.m22, this.m23,
            this.m30, this.m31, this.m32, this.m33,
        ]
    }
    
    translate(x, y, z) {
        const src = new Matrix()
        this.load(this, src)
        
        this.m30 += src.m00 * x + src.m10 * y + src.m20 * z
        this.m31 += src.m01 * x + src.m11 * y + src.m21 * z
        this.m32 += src.m02 * x + src.m12 * y + src.m22 * z
        this.m33 += src.m03 * x + src.m13 * y + src.m23 * z
        
        return this
    }
    
    rotate(angle, x, y, z) {
        const src = new Matrix()
        this.load(this, src)
    
        const c = Math.cos(angle)
        const s = Math.sin(angle)
        const oneminusc = 1.0 - c
        
        const xy = x * y
        const yz = y * z
        const xz = x * z
        
        const xs = x * s
        const ys = y * s
        const zs = z * s
    
        const f00 = x * x * oneminusc + c
        const f01 = xy * oneminusc + zs
        const f02 = xz * oneminusc - ys
        
        const f10 = xy * oneminusc - zs
        const f11 = y * y * oneminusc + c
        const f12 = yz * oneminusc + xs
        
        const f20 = xz * oneminusc + ys
        const f21 = yz * oneminusc - xs
        const f22 = z * z * oneminusc + c
    
        const t00 = src.m00 * f00 + src.m10 * f01 + src.m20 * f02
        const t01 = src.m01 * f00 + src.m11 * f01 + src.m21 * f02
        const t02 = src.m02 * f00 + src.m12 * f01 + src.m22 * f02
        const t03 = src.m03 * f00 + src.m13 * f01 + src.m23 * f02
        
        const t10 = src.m00 * f10 + src.m10 * f11 + src.m20 * f12
        const t11 = src.m01 * f10 + src.m11 * f11 + src.m21 * f12
        const t12 = src.m02 * f10 + src.m12 * f11 + src.m22 * f12
        const t13 = src.m03 * f10 + src.m13 * f11 + src.m23 * f12
        
        this.m20 = src.m00 * f20 + src.m10 * f21 + src.m20 * f22
        this.m21 = src.m01 * f20 + src.m11 * f21 + src.m21 * f22
        this.m22 = src.m02 * f20 + src.m12 * f21 + src.m22 * f22
        this.m23 = src.m03 * f20 + src.m13 * f21 + src.m23 * f22
        
        this.m00 = t00
        this.m01 = t01
        this.m02 = t02
        this.m03 = t03
        this.m10 = t10
        this.m11 = t11
        this.m12 = t12
        this.m13 = t13
        
        return this
    }
}