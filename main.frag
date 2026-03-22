#version 330 core

in vec3 FragPos;
in vec3 Normal;

in vec4 FragPosLightSpace;
uniform vec3 lightPos;
uniform vec3 viewPos;
uniform vec3 lightColor;
uniform vec3 objectColor;
uniform sampler2D shadowMap;

out vec4 FragColor;

float shadowTest(vec4 fragPosLightSpace) { vec3 projCoords = fragPosLightSpace.xyz / fragPosLightSpace.w;     projCoords = projCoords * 0.5 + 0.5;
    if (projCoords.z > 1.0) return 0.0;
    float currentDepth = projCoords.z;
    float bias = 0.005;     float shadow = 0.0;
    vec2 texelSize = 1.0 / textureSize(shadowMap, 0);
    for (int x = -1; x <= 1; x++) { for (int y = -1; y <= 1; y++) {
        float pcfDepth = texture(shadowMap,
        projCoords.xy + vec2(x, y) * texelSize).r;
        shadow += (currentDepth - bias > pcfDepth) ? 1.0 : 0.0;
    } }     shadow /= 9.0;
    return shadow;
}


void main() {

    vec3 norm    = normalize(Normal);
    vec3 lightDir = normalize(lightPos - FragPos);
    vec3 viewDir  = normalize(viewPos  - FragPos);
    vec3 reflDir  = reflect(-lightDir, norm);

    float ambient  = 0.15;
    float diff     = max(dot(norm, lightDir), 0.0);
    float spec     = pow(max(dot(viewDir, reflDir), 0.0), 32.0);

    float shadow = shadowTest(FragPosLightSpace);

    vec3 lighting = (ambient + (1.0 - shadow) * (diff + spec * 0.3)) * lightColor;
    FragColor = vec4(lighting * objectColor, 1.0);
}
