#version 410

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 textCoord;
layout(location = 2) in vec3 normal;
layout(location = 4) in vec3 offset;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

out vec2 uv;
out vec3 vertPos;
out vec3 normals;

void main() {
	uv = textCoord;
	normals = normal;

	vertPos = vec3(model * vec4(position, 1.0));
	gl_Position = projection * view * model * vec4(position + offset, 1.0);
}