export class Vector3 {
    x= 0
    y = 0
    z = 0
    
    constructor(x, y, z) {
        this.x = x
        this.y = y
        this.z = z
    }

    toString() {
        return `[${this.x}, ${this.y}, ${this.z}]`
    }
}