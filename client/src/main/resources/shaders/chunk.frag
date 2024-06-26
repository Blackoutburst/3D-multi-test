#version 410

#define ambientStrength 0.9
#define specularStrength 0.6

in vec3 FragPos;
in vec4 FragPosLightSpace;
in vec2 uv;
flat in float layer;
in vec3 norm;
in vec3 TangentLightPos;
in vec3 TangentViewPos;
in vec3 TangentFragPos;

uniform sampler2DArray text;
uniform sampler2D shadowMap;
uniform sampler2DArray normalMap;
uniform vec3 lightColor;
uniform vec4 color;
uniform vec3 viewPos;
uniform vec3 lightPos;

out vec4 FragColor;


float ShadowCalculation(vec4 fragPosLightSpace, vec3 normal)
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

    //normal
    //vec3 normal = normalize(norm);
    vec3 normal = texture(normalMap, vec3(uv, layer)).rgb;
    normal = normalize(normal * 2.0 - 1.0);

    //diffuse
    //vec3 lightDir = vec3(0.1, 0.4, 0.2);
    //vec3 lightDir = normalize(lightPos - FragPos);
    vec3 lightDir = normalize(TangentLightPos  - TangentFragPos);
    float diff = max(dot(lightDir, normal), 0.0);
    vec3 diffuse = diff * lightColor;

    //specular
    //vec3 viewDir = normalize(viewPos - FragPos);
    vec3 viewDir = normalize(TangentViewPos - TangentFragPos);
    vec3 halfwayDir = normalize(lightDir + viewDir);
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(normal, halfwayDir), 0.0), 32.0);
    vec3 specular = specularStrength * spec * lightColor;

    //shadow
    float shadow = ShadowCalculation(FragPosLightSpace, normal);

    //vec3 result = (ambient + diffuse + specular) * color.rgb;
    //vec3 result = (ambient + diffuse) * color.rgb;
    vec3 result = (ambient + (1.0 - shadow) * (diffuse + specular)) * color.rgb;
    //vec3 result = (ambient + (1.0 - shadow)) * color.rgb;
    FragColor = vec4(result, color.a) * texture(text, vec3(uv, layer));

    //vec3 rgb_normal = normal * 0.5 + 0.5;
    //FragColor = vec4(rgb_normal, 1.0);
}