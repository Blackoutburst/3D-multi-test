import { Matrix } from "./matrix.js";
import {Vector3} from "./vector3.js";

const keysPressed = {}
const speed = 0.05

export class Player {
    position = new Vector3(0, 20, 20)
    yaw = 0
    pitch = 0
    camera = new Matrix()
        .rotate((this.yaw * Math.PI) / 180, 1, 0, 0)
        .rotate((this.pitch * Math.PI) / 180, 0, 1, 0)
        .translate(-this.position.x, -this.position.y, -this.position.z)
    
    update() {
        if (keysPressed['z']) {
            this.position.x -= Math.sin(-(this.pitch * Math.PI) / 180) * speed
            this.position.z -= Math.cos(-(this.pitch * Math.PI) / 180) * speed
        }
        
        if (keysPressed['s']) {
            this.position.x += Math.sin(-(this.pitch * Math.PI) / 180) * speed
            this.position.z += Math.cos(-(this.pitch * Math.PI) / 180) * speed
        }
        
        if (keysPressed['q']) {
            this.position.x += Math.sin(-((this.pitch + 90) * Math.PI) / 180) * speed
            this.position.z += Math.cos(-((this.pitch + 90) * Math.PI) / 180) * speed
        }
        
        if (keysPressed['d']) {
            this.position.x += Math.sin(-((this.pitch - 90) * Math.PI) / 180) * speed
            this.position.z += Math.cos(-((this.pitch - 90) * Math.PI) / 180) * speed
        }
        
        if (keysPressed['shift']) {
            this.position.y -= speed
        }
        
        if (keysPressed[' ']) {
            this.position.y += speed
        }
    }
}

function handleKeyDown(event) {
    keysPressed[event.key.toLowerCase()] = true
}

function handleKeyUp(event) {
    keysPressed[event.key.toLowerCase()] = false
}


document.addEventListener('keydown', handleKeyDown)
document.addEventListener('keyup', handleKeyUp)
