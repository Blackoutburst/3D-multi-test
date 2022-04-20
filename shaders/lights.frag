#version 410

precision mediump float;

struct LightSource {
	vec2 position;
	vec3 color;
	float intensity;
};

uniform vec2 resolution;
uniform LightSource lights[100];

out vec4 FragColor;

void main() {
	vec2 p = (gl_FragCoord.xy) / min(resolution.x, resolution.y);
	vec3 color = vec3(0.0);

	for (int i = 0; i < 100; i++) {
		if (lights[i].color.r == 0.0 && lights[i].color.g == 0.0 && lights[i].color.b == 0.0)
			break;
		vec3 h = ((lights[i].intensity / 10.0) / abs(length(vec2((lights[i].position.x / resolution.x) * (max(resolution.x, resolution.y) / min(resolution.x, resolution.y)), (lights[i].position.y / resolution.y)) - p))) * lights[i].color;
		color += h;
	}

	FragColor = vec4(color, 1.0);
} 
