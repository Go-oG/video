precision mediump float;
varying vec2 vTextureCoord;

uniform lowp sampler2D sTexture;
uniform highp vec2 center;
uniform highp float radius;
uniform highp float aspectRatio;
uniform highp float refractiveIndex;

void main() {
    highp vec2 textureCoordinateToUse = vec2(vTextureCoord.x, (vTextureCoord.y * aspectRatio + 0.5 - 0.5 * aspectRatio));
    highp float distanceFromCenter = distance(center, textureCoordinateToUse);
    lowp float checkForPresenceWithinSphere = step(distanceFromCenter, radius);
    distanceFromCenter = distanceFromCenter / radius;
    highp float normalizedDepth = radius * sqrt(1.0 - distanceFromCenter * distanceFromCenter);
    highp vec3 sphereNormal = normalize(vec3(textureCoordinateToUse - center, normalizedDepth));
    highp vec3 refractedVector = refract(vec3(0.0, 0.0, -1.0), sphereNormal, refractiveIndex);
    gl_FragColor = texture2D(sTexture, (refractedVector.xy + 1.0) * 0.5) * checkForPresenceWithinSphere;
}