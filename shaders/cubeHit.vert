#version 410

layout(location = 0) in vec3 position;
layout(location = 3) in vec3 lcolor;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

out vec3 vertColor;

void main() {
	vertColor = lcolor;

	gl_Position = projection * view * model * vec4(position, 1.0);
}
