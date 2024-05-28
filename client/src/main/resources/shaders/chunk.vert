#version 410

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 textCoord;
layout(location = 2) in vec3 normal;
layout(location = 3) in float textId;

uniform mat4 view;
uniform mat4 projection;

out vec2 uv;
out vec3 vertPos;
out vec3 normals;
flat out float layer;

void main() {
	uv = textCoord;
	normals = normal;
	layer = textId;

	vertPos = vec3(vec4(position, 1.0));

	gl_Position = projection * view * vec4(position, 1.0);
}