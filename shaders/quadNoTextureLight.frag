#version 410

precision mediump float;

in vec2 vertPos;

uniform vec2 resolution;
uniform vec4 color;
uniform vec3 ambient;
uniform float radius;

out vec4 FragColor;

struct LightSource {
	vec2 position;
	vec3 color;
	float intensity;
};

uniform LightSource lights[100];

void main() {
	vec2 p = (gl_FragCoord.xy) / min(resolution.x, resolution.y);
	vec3 lightColor = ambient;

	for (int i = 0; i < 100; i++) {
		if (lights[i].color.r == 0.0 && lights[i].color.g == 0.0 && lights[i].color.b == 0.0)
			break;
		vec3 h = ((lights[i].intensity / 10.0) / abs(length(vec2((lights[i].position.x / resolution.x) * (max(resolution.x, resolution.y) / min(resolution.x, resolution.y)), (lights[i].position.y / resolution.y)) - p))) * lights[i].color;
		lightColor += h;
	}

	FragColor = vec4(lightColor, 1.0) * color;

	if (distance(vec2(0.0), vertPos) > radius)
		FragColor = vec4(0.0);
} 
