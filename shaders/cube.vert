#version 410

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texCoords;
layout(location = 3) in vec3 normals;

uniform mat4 model;
uniform mat4 projection;
uniform mat4 view;

out vec2 uv;
out vec3 vertPos;

void main() {
	vertPos = position;
	uv = texCoords;
	gl_Position = projection * view * model * vec4(position, 1.0);
}
