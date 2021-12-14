#version 460 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 textCoord;
layout(location = 2) in vec3 normal;
layout(location = 3) in vec3 color;
layout(location = 4) in vec3 offset;
layout(location = 5) in vec2 textOffset;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

out vec3 vertPos;
out vec2 uv;
out vec3 normals;
out vec3 vcolor;
out vec2 uvOffset;

void main() {
	vertPos = vec3(model * vec4(position + offset, 1.0));
	uv = textCoord;
	normals = normal;
	vcolor = color;
	uvOffset = textOffset;

	gl_Position = projection * view * model * vec4(position + offset, 1.0);
}
