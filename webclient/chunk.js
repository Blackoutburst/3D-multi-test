import { Vector3 } from "./vector3.js"

export class Chunk {
    position = new Vector3()
    blocks = [[[]]]
    blockAsList = []
    
    constructor(position, blocks, blockAsList) {
        this.position = position
        this.blocks = blocks
        this.blockAsList = blockAsList
    }
}