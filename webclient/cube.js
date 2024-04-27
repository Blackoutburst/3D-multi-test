import { Matrix } from "./matrix.js";

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

const vertexShaderSource =
`#version 300 es

in vec3 position;
in vec2 textCoord;
in vec3 normal;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

out vec2 uv;
out vec3 vertPos;
out vec3 normals;
out vec4 vPos;

void main() {
    uv = textCoord;
    normals = normal;

    vPos = vec4(position, 1.0);
    vertPos = vec3(model * vec4(position, 1.0));

    gl_Position = projection * view * model * vec4(position, 1.0);
}
`

const fragmentShaderSource =
`#version 300 es

#define ambientStrength 0.8
#define specularStrength 0.2

precision mediump float;

in vec4 vPos;
in vec3 vertPos;
in vec2 uv;
in vec3 normals;

uniform sampler2D text;
uniform vec3 lightColor;
uniform vec3 viewPos;

uniform vec4 color;

out vec4 FragColor;

void main() {

    vec3 norm = normalize(normals);
    vec3 lightDir = vec3(0.1, 0.4, 0.2);

    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * lightColor;

    vec3 viewDir = normalize(viewPos - vertPos);
    vec3 reflectDir = reflect(-lightDir, norm);

    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32.0);
    vec3 specular = specularStrength * spec * lightColor;

    vec3 ambient = ambientStrength * lightColor;

    vec3 result = (ambient + diffuse) * color.xyz;

    FragColor = vec4(result, color.w) * texture(text, uv);
}
`

let cameraX = 0
let cameraY = 0
let cameraZ = 0
let yaw = 0
let pitch = 0

const cameraSpeed = 0.05
const mouseSensitivity = 0.1

function createShaderProgram(gl) {
    const vertexShader = loadShader(gl, gl.VERTEX_SHADER, vertexShaderSource)
    const fragmentShader = loadShader(gl, gl.FRAGMENT_SHADER, fragmentShaderSource)
    const shaderProgram = gl.createProgram()
    
    gl.attachShader(shaderProgram, vertexShader)
    gl.attachShader(shaderProgram, fragmentShader)
    gl.linkProgram(shaderProgram)
    
    if (!gl.getProgramParameter(shaderProgram, gl.LINK_STATUS)) {
        alert(`Unable to initialize the shader program: ${gl.getProgramInfoLog(shaderProgram,)}`)
        return null
    }

    return shaderProgram
}

function loadShader(gl, type, source) {
    const shader = gl.createShader(type)

    gl.shaderSource(shader, source)
    gl.compileShader(shader)

    if (!gl.getShaderParameter(shader, gl.COMPILE_STATUS)) {
        console.log(`An error occurred compiling the shaders: ${gl.getShaderInfoLog(shader)}`)
        alert(`An error occurred compiling the shaders: ${gl.getShaderInfoLog(shader)}`)
        gl.deleteShader(shader)
        return null
    }

    return shader
}


function setUniformMat4(gl, program, name, matrix) {
    const loc = gl.getUniformLocation(program, name)
    gl.uniformMatrix4fv(loc, false, matrix.values())
}

function setUniform3f(gl, program, name, x, y, z) {
    const loc = gl.getUniformLocation(program, name)
    gl.uniform3f(loc, x, y, z)
}

function setUniform1i(gl, program, name, value) {
    const loc = gl.getUniformLocation(program, name)
    gl.uniform1i(loc, value)
}

function setUniform4f(gl, program, name, x, y, z, w) {
    const loc = gl.getUniformLocation(program, name)
    gl.uniform4f(loc, x, y, z, w)
}

export function draw(gl) {
    const texture = gl.createTexture()
    const image = new Image()
    image.src = './stonebrick.png'
    image.onload = function() {
        gl.bindTexture(gl.TEXTURE_2D, texture)
        gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, image)
        gl.generateMipmap(gl.TEXTURE_2D)



        const shaderProgram = createShaderProgram(gl)
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

        const fov = (90 * Math.PI) / 180
        const zNear = 0.1
        const zFar = 1000

        const projection = new Matrix()
        .projection(gl, fov, zNear, zFar)

        const view = new Matrix()
            .rotate((yaw * Math.PI) / 180, 1, 0, 0)
            .rotate((pitch * Math.PI) / 180, 0, 1, 0)
            .translate(-cameraX,-cameraY,-cameraZ)

        const model = new Matrix()
        .translate(0,0,-6)
        .rotate(1, 1, 0, 0)
        .rotate(1, 0, 1, 0)
        .rotate(1, 0, 0, 1)

        gl.useProgram(shaderProgram)

        setUniformMat4(gl, shaderProgram, "projection", projection)
        setUniformMat4(gl, shaderProgram, "view", view)
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

        gl.drawArrays(gl.TRIANGLES, 0, 36)
        gl.bindTexture(gl.TEXTURE_2D, null)
        gl.useProgram(null)
        
        requestAnimationFrame(() => draw(gl))
    }
}

const keysPressed = {}

function handleKeyDown(event) {
    keysPressed[event.key.toLowerCase()] = true
}

function handleKeyUp(event) {
    keysPressed[event.key.toLowerCase()] = false
}

export function updatePosition(e) {
    pitch += e.movementX * mouseSensitivity
    yaw += e.movementY * mouseSensitivity

    yaw = Math.max(-89, Math.min(89, yaw))
}


document.addEventListener('keydown', handleKeyDown);
document.addEventListener('keyup', handleKeyUp);

export function updateCameraPosition() {
    if (keysPressed['z']) {
        cameraX -= Math.sin(-(pitch * Math.PI) / 180) * cameraSpeed
        cameraZ -= Math.cos(-(pitch * Math.PI) / 180) * cameraSpeed
    }
    
    if (keysPressed['s']) {
        cameraX += Math.sin(-(pitch * Math.PI) / 180) * cameraSpeed
        cameraZ += Math.cos(-(pitch * Math.PI) / 180) * cameraSpeed
    }
    
    if (keysPressed['q']) {
        cameraX += Math.sin(-((pitch + 90) * Math.PI) / 180) * cameraSpeed
        cameraZ += Math.cos(-((pitch + 90) * Math.PI) / 180) * cameraSpeed
    }
    
    if (keysPressed['d']) {
        cameraX += Math.sin(-((pitch - 90) * Math.PI) / 180) * cameraSpeed
        cameraZ += Math.cos(-((pitch - 90) * Math.PI) / 180) * cameraSpeed
    }
    
    if (keysPressed['shift']) {
        cameraY -= cameraSpeed
    }
    
    if (keysPressed[' ']) {
        cameraY += cameraSpeed
    }
}