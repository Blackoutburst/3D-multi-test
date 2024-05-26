#version 410

#define ambientStrength 0.8
#define specularStrength 0.2

precision mediump float;

in vec4 vPos;
in vec3 vertPos;
in vec2 uv;
in vec3 normals;
flat in float blockType;

uniform sampler2D text;
uniform vec3 lightColor;
uniform vec3 viewPos;

uniform vec4 color;

out vec4 FragColor;

void main() {

    vec3 norm = normalize(normals);
    vec3 lightDir = vec3(0.1, 0.4, 0.2);

    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * lightColor;

    vec3 viewDir = normalize(viewPos - vertPos);
    vec3 reflectDir = reflect(-lightDir, norm);

    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32.0);
    vec3 specular = specularStrength * spec * lightColor;

    vec3 ambient = ambientStrength * lightColor;

    vec3 finalColor = color.xyz;
    if (blockType == 2.0)
        finalColor = vec3(0.82156862745, 0.51568627451, 0.3);
    if (blockType == 3)
        finalColor = vec3(0.5, 0.5, 0.5);

    vec3 result = (ambient + diffuse) * finalColor;

    FragColor = vec4(result, color.w) * texture(text, uv);
}