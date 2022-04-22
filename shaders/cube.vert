#version 410

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 textCoord;
layout(location = 2) in vec3 normal;
layout(location = 3) in vec3 offset;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform float time;

out vec2 uv;
out vec3 vertPos;
out vec3 normals;
out vec4 vPos;

void main() {
	uv = textCoord;
	normals = normal;

	vPos = model * vec4(position + offset, 1.0);
	vertPos = vec3(model * vec4(position, 1.0));

	vec3 wave = vec3(0.0, sin(vPos.x / 4.0 + cos((vPos.z + time) / 4.0) + time), 0.0);

	gl_Position = projection * view * model * vec4(position + wave + offset, 1.0);
}