#version 410

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 textCoord;
layout(location = 2) in vec3 normal;
layout(location = 3) in float textId;

uniform mat4 view;
uniform mat4 projection;

uniform mat4 lightView;
uniform mat4 lightProjection;

out vec2 uv;
out vec3 normals;
flat out float layer;
out vec3 FragPos;
out vec4 FragPosLightSpace;

void main() {
	uv = textCoord;
	normals = normal;
	layer = textId;

	FragPos = vec3(vec4(position, 1.0));
	FragPosLightSpace = lightProjection * lightView * vec4(FragPos, 1.0);

	gl_Position = projection * view * vec4(position, 1.0);
}