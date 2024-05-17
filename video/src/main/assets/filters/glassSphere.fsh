varying highp vec2 vTextureCoord;
uniform sampler2D sTexture;

uniform highp vec2 center;
uniform highp float radius;
uniform highp float aspectRatio;
uniform highp float refractiveIndex;

const highp vec3 lightPosition = vec3(-0.5, 0.5, 1.0);
const highp vec3 ambientLightPosition = vec3(0.0, 0.0, 1.0);

void main() {
 vec2 textureCoordinateToUse = vec2(vTextureCoord.x, (vTextureCoord.y * aspectRatio + 0.5 - 0.5 * aspectRatio));
 float distanceFromCenter = distance(center, textureCoordinateToUse);
 float checkForPresenceWithinSphere = step(distanceFromCenter, radius);
 
 distanceFromCenter = distanceFromCenter / radius;

 float normalizedDepth = radius * sqrt(1.0 - distanceFromCenter * distanceFromCenter);
 vec3 sphereNormal = normalize(vec3(textureCoordinateToUse - center, normalizedDepth));

 vec3 refractedVector = 2.0 * refract(vec3(0.0, 0.0, -1.0), sphereNormal, refractiveIndex);
 refractedVector.xy = -refractedVector.xy;

 vec3 finalSphereColor = texture2D(sTexture, (refractedVector.xy + 1.0) * 0.5).rgb;
 
 // Grazing angle lighting
 float lightingIntensity = 2.5 * (1.0 - pow(clamp(dot(ambientLightPosition, sphereNormal), 0.0, 1.0), 0.25));
 finalSphereColor += lightingIntensity;
 
 // Specular lighting
 lightingIntensity  = clamp(dot(normalize(lightPosition), sphereNormal), 0.0, 1.0);
 lightingIntensity  = pow(lightingIntensity, 15.0);
 finalSphereColor += vec3(0.8, 0.8, 0.8) * lightingIntensity;
 
 gl_FragColor = vec4(finalSphereColor, 1.0) * checkForPresenceWithinSphere;
}