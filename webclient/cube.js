import { Matrix } from "./matrix.js"
import { loadTexture } from "./texture.js"
import { createShaderProgram, setUniform1i, setUniform3f, setUniform4f, setUniformMat4 } from "./shader.js"
import { Vector3 } from "./vector3.js"

const vertices = [
    //FRONT
    -0.5, -0.5, -0.5, 0.0, 0.0, 0.0, 0.0, -1.0,
    0.5, 0.5, -0.5, 1.0, 1.0, 0.0, 0.0, -1.0,
    0.5, -0.5, -0.5, 1.0, 0.0, 0.0, 0.0, -1.0,
    0.5, 0.5, -0.5, 1.0, 1.0, 0.0, 0.0, -1.0,
    -0.5, -0.5, -0.5, 0.0, 0.0, 0.0, 0.0, -1.0,
    -0.5, 0.5, -0.5, 0.0, 1.0, 0.0, 0.0, -1.0,
    //BACK
    -0.5, -0.5, 0.5, 0.0, 0.0, 0.0, 0.0, 1.0,
    0.5, -0.5, 0.5, 1.0, 0.0, 0.0, 0.0, 1.0,
    0.5, 0.5, 0.5, 1.0, 1.0, 0.0, 0.0, 1.0,
    0.5, 0.5, 0.5, 1.0, 1.0, 0.0, 0.0, 1.0,
    -0.5, 0.5, 0.5, 0.0, 1.0, 0.0, 0.0, 1.0,
    -0.5, -0.5, 0.5, 0.0, 0.0, 0.0, 0.0, 1.0,
    //LEFT
    -0.5, 0.5, 0.5, 1.0, 0.0, -1.0, 0.0, 0.0,
    -0.5, 0.5, -0.5, 1.0, 1.0, -1.0, 0.0, 0.0,
    -0.5, -0.5, -0.5, 0.0, 1.0, -1.0, 0.0, 0.0,
    -0.5, -0.5, -0.5, 0.0, 1.0, -1.0, 0.0, 0.0,
    -0.5, -0.5, 0.5, 0.0, 0.0, -1.0, 0.0, 0.0,
    -0.5, 0.5, 0.5, 1.0, 0.0, -1.0, 0.0, 0.0,
    //RIGHT
    0.5, 0.5, 0.5, 1.0, 0.0, 1.0, 0.0, 0.0,
    0.5, -0.5, -0.5, 0.0, 1.0, 1.0, 0.0, 0.0,
    0.5, 0.5, -0.5, 1.0, 1.0, 1.0, 0.0, 0.0,
    0.5, -0.5, -0.5, 0.0, 1.0, 1.0, 0.0, 0.0,
    0.5, 0.5, 0.5, 1.0, 0.0, 1.0, 0.0, 0.0,
    0.5, -0.5, 0.5, 0.0, 0.0, 1.0, 0.0, 0.0,
    //BOTTOM
    -0.5, -0.5, -0.5, 0.0, 1.0, 0.0, -1.0, 0.0,
    0.5, -0.5, -0.5, 1.0, 1.0, 0.0, -1.0, 0.0,
    0.5, -0.5, 0.5, 1.0, 0.0, 0.0, -1.0, 0.0,
    0.5, -0.5, 0.5, 1.0, 0.0, 0.0, -1.0, 0.0,
    -0.5, -0.5, 0.5, 0.0, 0.0, 0.0, -1.0, 0.0,
    -0.5, -0.5, -0.5, 0.0, 1.0, 0.0, -1.0, 0.0,
    //TOP
    -0.5, 0.5, -0.5, 0.0, 1.0, 0.0, 1.0, 0.0,
    0.5, 0.5, 0.5, 1.0, 0.0, 0.0, 1.0, 0.0,
    0.5, 0.5, -0.5, 1.0, 1.0, 0.0, 1.0, 0.0,
    0.5, 0.5, 0.5, 1.0, 0.0, 0.0, 1.0, 0.0,
    -0.5, 0.5, -0.5, 0.0, 1.0, 0.0, 1.0, 0.0,
    -0.5, 0.5, 0.5, 0.0, 0.0, 0.0, 1.0, 0.0,
]

let texture
let shaderProgram

let projection
let model

let world = []

function setMatrices(gl) {
    const fov = (90 * Math.PI) / 180
    const zNear = 0.1
    const zFar = 1000

    projection = new Matrix()
    .projection(gl, fov, zNear, zFar)

    model = new Matrix()
}

export function initRender(gl) {
    const size = 50

    for (let x = -size; x < size; x++) {
        for (let y = -size; y < size; y++) {
            for (let z = -size; z < size; z++) {
                world.push(new Vector3(x, y, z))
            }
        }
    }

    texture = loadTexture(gl, './stonebrick.png')
    shaderProgram = createShaderProgram(gl)

    const buffer = gl.createBuffer()
    gl.bindBuffer(gl.ARRAY_BUFFER, buffer)
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW)

    // Position
    gl.vertexAttribPointer(0, 3, gl.FLOAT, false, 32, 0)
    gl.enableVertexAttribArray(0)

    // UV
    gl.vertexAttribPointer(1, 2, gl.FLOAT, false, 32, 12)
    gl.enableVertexAttribArray(1)

    // Normal
    gl.vertexAttribPointer(2, 3, gl.FLOAT, false, 32, 20)
    gl.enableVertexAttribArray(2)

    gl.bindBuffer(gl.ARRAY_BUFFER, null)

    setMatrices(gl)
    setOffset(gl, world)
}


function setOffset(gl, blocks) {
    const size = blocks.length
    const translation = new Float32Array(size * 3)

    let idx = 0;
    for (let i = 0; i < size; i++) {
        translation[idx] = blocks[i].x
        translation[idx + 1] = blocks[i].y
        translation[idx + 2] = blocks[i].z
        idx += 3
    }

    const instanceVBO = gl.createBuffer()

    gl.bindBuffer(gl.ARRAY_BUFFER, instanceVBO)
    gl.bufferData(gl.ARRAY_BUFFER, translation, gl.STATIC_DRAW)

    gl.enableVertexAttribArray(3)
    gl.vertexAttribPointer(3, 3, gl.FLOAT, false, 0, 0)
    gl.vertexAttribDivisor(3, 1)

    gl.bindBuffer(gl.ARRAY_BUFFER, null)
}


export function draw(gl, player) {

    player.camera.identity()
        .rotate((player.yaw * Math.PI) / 180, 1, 0, 0)
        .rotate((player.pitch * Math.PI) / 180, 0, 1, 0)
        .translate(-player.position.x, -player.position.y, -player.position.z)

    gl.useProgram(shaderProgram)

    setUniformMat4(gl, shaderProgram, "projection", projection)
    setUniformMat4(gl, shaderProgram, "view", player.camera)
    setUniformMat4(gl, shaderProgram, "model", model)

    setUniform4f(gl, shaderProgram, "color", 1, 1, 1, 1)
    setUniform3f(gl, shaderProgram, "lightColor", 1, 1, 1)
    setUniform3f(gl, shaderProgram, "viewPos", 0, 0, 0)
    setUniform1i(gl, shaderProgram, "text", texture)

    gl.activeTexture(gl.TEXTURE0)
    gl.bindTexture(gl.TEXTURE_2D, texture)

    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE)
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE)
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.NEAREST)
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.NEAREST)

    //gl.drawArrays(gl.TRIANGLES, 0 , 36)
    gl.drawArraysInstanced(gl.TRIANGLES, 0, 36, world.length)
    gl.bindTexture(gl.TEXTURE_2D, null)

    requestAnimationFrame(() => draw(gl, player))
}