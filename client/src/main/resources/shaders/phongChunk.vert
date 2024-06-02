#version 410

layout(location = 0) in int data;

uniform mat4 view;
uniform mat4 projection;
uniform vec3 chunkPos;

out vec3 FragPos;
out vec2 uv;
out vec3 norm;
flat out float layer;

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
	//XXXXX YYYYY ZZZZZ UUUUU VVVVV NNN TTT

	int X = data & 31;
	int Y = (data >> 5) & 31;
	int Z = (data >> 10) & 31;
	int U = (data >> 15) & 31;
	int V = (data >> 20) & 31;
	int N = (data >> 25) & 7;
	int T = (data >> 28) & 15;

	FragPos = vec3(X, Y, Z) + chunkPos;
	uv = vec2(U, V);
	norm = getNormal(N);
	layer = T;

	gl_Position = projection * view * vec4(FragPos, 1.0);
}