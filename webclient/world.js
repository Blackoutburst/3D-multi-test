import { Block } from "./block.js"
import { Vector3 } from "./vector3.js"
import { Chunk } from "./chunk.js"
import { setOffset } from "./cube.js"
import { gl } from "./main.js"
import { AIR } from "./blocktypes.js"

export class World {
    chunkSize = 16
    chunks = new Map()
    worldBlocks = []
    
    updateChunk(position, blockData) {
        const blocks = Array.from({length: this.chunkSize}, () => 
                Array.from({length: this.chunkSize}, () => 
                    Array.from({length: this.chunkSize}, () => new Block(AIR, new Vector3(0,0,0)))))
        
        let index = 0;
    
        for (let x = 0; x < this.chunkSize; x++) {
            for (let y = 0; y < this.chunkSize; y++) {
                for (let z = 0; z < this.chunkSize; z++) {
                    const blockType = blockData[index]
                    blocks[x][y][z] = new Block(blockType, new Vector3(position.x + x, position.y + y, position.z + z))
                    index++
                }
            }
        }
        
        const blockAsList = blocks.flat(2).filter(block => block.type !== AIR)
    
        this.chunks.set(position.toString(), new Chunk(position, blocks, blockAsList))
    
        this.worldBlocks = Array.from(this.chunks.values()).flatMap(chunk => chunk.blockAsList)
        
        this.update()
    }
    
    update() {
        setOffset(gl, this.worldBlocks)
    }
}