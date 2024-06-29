#version 410

#define ambientStrength 0.6
#define specularStrength 0.2

in vec3 FragPos;
in vec2 uv;
in vec3 norm;
flat in float layer;

uniform sampler2DArray text;
uniform vec3 lightColor;
uniform vec3 viewPos;
uniform vec3 lightPos;

uniform vec4 color;

out vec4 FragColor;

void main() {
    FragColor = vec4(1.0) * texture(text, vec3(uv * 1000.0, layer)) * vec4(vec3(clamp((FragPos.y + 30.0) / 40.0, 0.2, 1.2)), 1.0);
}