#version 410 core

uniform sampler2D diffuseMap;

in vec2 uv;
in vec2 fragPos;

out vec4 FragColor;


#define radius 1
#define borderThickness 1

void main() {
    //FragColor = vec4(uv, 0.0, 1.0);

    if (distance(vec2(0.5), gl_FragCoord.xy / vec2(400)) >= 0.5)
        discard;

    FragColor = vec4(1.0) * texture(diffuseMap, vec2(uv));
}