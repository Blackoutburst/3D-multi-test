#version 410

#define ambientStrength 1.0
#define specularStrength 1.0

precision mediump float;

in vec3 vertPos;
in vec2 uv;
in vec3 normals;
in vec4 vPos;

uniform sampler2D text;
uniform vec3 lightColor;
uniform vec3 lightPos;
uniform vec3 viewPos;

uniform vec3 color;

out vec4 FragColor;

void main() {

    vec3 norm = normalize(normals);
    vec3 lightDir = normalize(lightPos - vertPos);

    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * lightColor;

    vec3 viewDir = normalize(viewPos - vertPos);
    vec3 reflectDir = reflect(-lightDir, norm);

    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32);
    vec3 specular = specularStrength * spec * lightColor;

    vec3 ambient = ambientStrength * lightColor;

    vec3 result = (ambient + diffuse) * color * (vec3(1) * (vPos.y + 2.2) * 1.5) + specular;

    FragColor = vec4(result, 0.8);
}