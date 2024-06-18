precision mediump float;
varying highp vec2 vTextureCoord;
uniform sampler2D sTexture;

uniform vec2 center;
uniform float radius;
uniform float aspectRatio;
uniform float refractiveIndex;

const vec3 lightPosition = vec3(-0.5, 0.5, 1.0);
const vec3 ambientLightPosition = vec3(0.0, 0.0, 1.0);

void main() {
    vec2 uv = vec2(vTextureCoord.x, (vTextureCoord.y * aspectRatio + 0.5 - 0.5 * aspectRatio));
    float disFromCenter = distance(center, uv);
    float presenceWithinSphere = step(disFromCenter, radius);

    disFromCenter = disFromCenter / radius;

    float normalizedDepth = radius * sqrt(1.0 - disFromCenter * disFromCenter);
    vec3 sphereNormal = normalize(vec3(uv - center, normalizedDepth));

    vec3 refractedVector = 2.0 * refract(vec3(0.0, 0.0, -1.0), sphereNormal, refractiveIndex);
    refractedVector.xy = -refractedVector.xy;

    vec3 finalColor = texture2D(sTexture, (refractedVector.xy + 1.0) * 0.5).rgb;

    // Grazing angle lighting
    float lightIntensity = 2.5 * (1.0 - pow(clamp(dot(ambientLightPosition, sphereNormal), 0.0, 1.0), 0.25));
    finalColor += lightIntensity;

    // Specular lighting
    lightIntensity = clamp(dot(normalize(lightPosition), sphereNormal), 0.0, 1.0);
    lightIntensity = pow(lightIntensity, 15.0);
    finalColor += vec3(0.8, 0.8, 0.8) * lightIntensity;

    gl_FragColor = vec4(finalColor, 1.0) * presenceWithinSphere;
}