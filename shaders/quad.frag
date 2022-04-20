#version 410

precision mediump float;

in vec2 uv;
in vec2 vertPos;

uniform vec4 color;
uniform sampler2D text;
uniform float radius;

out vec4 FragColor;

void main() {
	FragColor = color * texture(text, uv);
	if (distance(vec2(0.0), vertPos) > radius)
		FragColor = vec4(0.0);
} 
