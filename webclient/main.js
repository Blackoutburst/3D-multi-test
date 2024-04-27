import {draw, updateCameraPosition, updatePosition} from "./cube.js"

const canvas = document.querySelector("#glcanvas")

function main() {
    const gl = canvas.getContext("webgl2")

    if (gl === null) {
        alert("Unable to initialize WebGL. Your browser or machine may not support it.",)
        return
    }
    
    document.addEventListener('pointerlockchange', lockChangeAlert, false)
    document.addEventListener('pointerlockerror', lockError, false)
    
    canvas.addEventListener('click', () => {
        canvas.requestPointerLock()
    })
    
    gl.clearColor(0, 0, 0, 1)
    gl.clearDepth(1)
    gl.enable(gl.CULL_FACE)
    gl.enable(gl.DEPTH_TEST)
    gl.depthFunc(gl.LEQUAL)
    gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT)
    
    loop(gl)
}


function lockChangeAlert() {
    if (document.pointerLockElement === canvas) {
        console.log('The pointer lock status is now locked');
        document.addEventListener("mousemove", updatePosition, false);
    } else {
        console.log('The pointer lock status is now unlocked');  
        document.removeEventListener("mousemove", updatePosition, false);
    }
}

function lockError() {
    console.error('Pointer lock failed.');
}

function loop(gl) {
    requestAnimationFrame(loop);
    updateCameraPosition()
    
    draw(gl)
}

main()