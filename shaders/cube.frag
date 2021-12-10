#version 410

precision mediump float;

in vec3 vertPos;
in vec2 uv;

uniform sampler2D text;

out vec4 FragColor;

void main() {
	FragColor = vec4(1.0) * texture(text, uv);
} 
