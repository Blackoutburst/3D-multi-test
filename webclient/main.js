import { draw, initRender } from "./cube.js"
import { Player } from "./player.js"
import { connectWebSocket } from "./websocket.js"
import { World } from "./world.js"


const canvas = document.querySelector("#glcanvas")
const sensitivity = 0.1
const player = new Player()

export const world = new World()

export const gl = canvas.getContext("webgl2")

function main() {

    if (gl === null) {
        alert("Unable to initialize WebGL. Your browser or machine may not support it.",)
        return
    }
    
    connectWebSocket()

    initRender(gl)

    canvas.addEventListener('click', () => {
        canvas.requestPointerLock()
    })

    document.addEventListener('pointerlockchange', lockChangeAlert, false)
    document.addEventListener('pointerlockerror', lockError, false)
    
    gl.clearColor(0.67, 0.80, 0.92, 1)
    gl.clearDepth(1)
    gl.enable(gl.CULL_FACE)
    gl.enable(gl.DEPTH_TEST)
    gl.depthFunc(gl.LEQUAL)
    
    loop(gl)
}

function loop(gl) {
    gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT)

    player.update()

    draw(gl, player, world.worldBlocks.length)

    requestAnimationFrame(() => loop(gl))
}

function updateMouse(e) {
    player.pitch += e.movementX * sensitivity
    player.yaw += e.movementY * sensitivity

    player.yaw = Math.max(-89, Math.min(89,  player.yaw))
}

function lockChangeAlert() {
    if (document.pointerLockElement === canvas) {
        document.addEventListener("mousemove", updateMouse, false);
    } else {
        document.removeEventListener("mousemove", updateMouse, false);
    }
}

function lockError() {
    console.error('Pointer lock failed.');
}


main()