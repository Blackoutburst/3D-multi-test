import { Vector3 } from "./vector3.js"
import { AIR } from "./blocktypes.js"

export class Block {
    type = AIR
    position = new Vector3()
    
    constructor(type, position) {
        this.type = type
        this.position = position
    }
}