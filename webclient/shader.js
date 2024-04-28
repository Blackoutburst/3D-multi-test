const vertexShaderSource =
`#version 300 es

in vec3 position;
in vec2 textCoord;
in vec3 normal;
in vec3 offset;

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

    gl_Position = projection * view * model * vec4(position + offset, 1.0);
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

export function createShaderProgram(gl) {
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

export function setUniformMat4(gl, program, name, matrix) {
    const loc = gl.getUniformLocation(program, name)
    gl.uniformMatrix4fv(loc, false, matrix.values())
}

export function setUniform3f(gl, program, name, x, y, z) {
    const loc = gl.getUniformLocation(program, name)
    gl.uniform3f(loc, x, y, z)
}

export function setUniform1i(gl, program, name, value) {
    const loc = gl.getUniformLocation(program, name)
    gl.uniform1i(loc, value)
}

export function setUniform4f(gl, program, name, x, y, z, w) {
    const loc = gl.getUniformLocation(program, name)
    gl.uniform4f(loc, x, y, z, w)
}