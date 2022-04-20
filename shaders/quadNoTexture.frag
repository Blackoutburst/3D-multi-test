#version 410

precision mediump float;

in vec2 vertPos;

uniform vec4 color;
uniform float radius;

out vec4 FragColor;

void main() {
	FragColor = color;
	if (distance(vec2(0.0), vertPos) > radius)
		FragColor = vec4(0.0);
} 
