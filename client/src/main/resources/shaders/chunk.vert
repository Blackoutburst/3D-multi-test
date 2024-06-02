#version 410

layout(location = 0) in vec3 aPos;
layout(location = 1) in vec2 aTexCoord;
layout(location = 2) in float aNormal;
layout(location = 3) in float aTexId;
layout(location = 4) in vec3 aTangent;
layout(location = 5) in vec3 aBitangent;

uniform mat4 view;
uniform mat4 projection;

uniform mat4 lightView;
uniform mat4 lightProjection;

uniform vec3 viewPos;
uniform vec3 lightPos;

out vec2 uv;
flat out float layer;
out vec3 FragPos;
out vec3 norm;
out vec4 FragPosLightSpace;

out vec3 TangentLightPos;
out vec3 TangentViewPos;
out vec3 TangentFragPos;

vec3 getNormal(int index) {
	const vec3 normals[6] = vec3[6](
		vec3(0.0f, 1.0f, 0.0f), // TOP
		vec3(0.0f, 0.0f, -1.0f), // FRONT
		vec3(0.0f, 0.0f, 1.0f), // BACK
		vec3(-1.0f, 0.0f, 0.0f), // LEFT
		vec3(1.0f, 0.0f, 0.0f), // RIGHT
		vec3(0.0f, -1.0f, 0.0f) // BOTTOM
	);
	return normals[index];
}

void main() {

	uv = aTexCoord;
	layer = aTexId;
	norm = getNormal(int(aNormal));

	FragPos = vec3(vec4(aPos, 1.0));
	FragPosLightSpace = lightProjection * lightView * vec4(FragPos, 1.0);

	vec3 T = normalize(aTangent);
	vec3 N = normalize(getNormal(int(aNormal)));
	T = normalize(T - dot(T, N) * N);
	vec3 B = cross(N, T);

	mat3 TBN = transpose(mat3(T, B, N));
	TangentLightPos = TBN * lightPos;
	TangentViewPos = TBN * viewPos;
	TangentFragPos = TBN * FragPos;

	gl_Position = projection * view * vec4(aPos, 1.0);
}