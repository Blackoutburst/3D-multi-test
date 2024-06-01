#version 410

#define ambientStrength 0.8
#define specularStrength 0.8

in vec3 FragPos;
in vec4 FragPosLightSpace;
in vec2 uv;
in vec3 normals;
flat in float layer;

uniform sampler2DArray text;
uniform sampler2D shadowMap;
uniform vec3 lightColor;
uniform vec3 viewPos;
uniform vec4 color;
uniform vec3 lightPos;

out vec4 FragColor;

float ShadowCalculation(vec4 fragPosLightSpace)
{
    // perform perspective divide
    vec3 projCoords = fragPosLightSpace.xyz / fragPosLightSpace.w;
    // transform to [0,1] range
    projCoords = projCoords * 0.5 + 0.5;
    // get closest depth value from light's perspective (using [0,1] range fragPosLight as coords)
    float closestDepth = texture(shadowMap, projCoords.xy).r;
    // get depth of current fragment from light's perspective
    float currentDepth = projCoords.z;
    // calculate bias (based on depth map resolution and slope)
    vec3 normal = normalize(normals);
    vec3 lightDir = normalize(lightPos - FragPos);
    float bias = max(0.05 * (1.0 - dot(normal, lightDir)), 0.005);
    // check whether current frag pos is in shadow
    // float shadow = currentDepth - bias > closestDepth  ? 1.0 : 0.0;
    // PCF
    float shadow = 0.0;
    vec2 texelSize = 1.0 / textureSize(shadowMap, 0);
    for(int x = -1; x <= 1; ++x)
    {
        for(int y = -1; y <= 1; ++y)
        {
            float pcfDepth = texture(shadowMap, projCoords.xy + vec2(x, y) * texelSize).r;
            shadow += currentDepth - bias > pcfDepth  ? 1.0 : 0.0;
        }
    }
    shadow /= 9.0;

    // keep the shadow at 0.0 when outside the far_plane region of the light's frustum.
    if(projCoords.z > 1.0)
    shadow = 0.0;

    return shadow;
}

void main() {
    if (texture(text, vec3(uv, layer)).a <= 0.5) {
        discard;
    }

    //ambient
    vec3 ambient = ambientStrength * lightColor;

    //diffuse
    vec3 norm = normalize(normals);
    vec3 lightDir = normalize(lightPos - FragPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * lightColor;

    //specular
    vec3 viewDir = normalize(viewPos - FragPos);
    vec3 halfwayDir = normalize(lightDir + viewDir);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(norm, halfwayDir), 0.0), 32.0);
    vec3 specular = specularStrength * spec * lightColor;

    //shadow
    float shadow = ShadowCalculation(FragPosLightSpace);

    vec3 result = (ambient + (1.0 - shadow) * (diffuse + specular)) * color.rgb;
    //vec3 result = (ambient + (1.0 - shadow)) * color.rgb;
    FragColor = vec4(result, color.a) * texture(text, vec3(uv, layer));
}