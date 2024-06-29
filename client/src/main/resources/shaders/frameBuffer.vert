#version 410 core

layout(location = 0) in vec2 aPos;
layout(location = 1) in vec2 aUv;

out vec2 uv;
out vec2 fragPos;

void main() {
    uv = aUv;
    fragPos = aPos;

    gl_Position = vec4(aPos, 1.0, 1.0);
}