#version 410

in vec2 uv;

uniform sampler2D text;

uniform float time;

out vec4 FragColor;

void main() {
    FragColor = texture(text, uv + sin(time) / 2.0) * vec4(0.0, 0.8, 1.0, 0.7);
}