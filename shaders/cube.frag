#version 410

precision mediump float;

in vec2 uv;
in vec3 vertPos;

uniform sampler2D text;

out vec4 FragColor;

void main() {
    FragColor = texture(text, uv);
}
