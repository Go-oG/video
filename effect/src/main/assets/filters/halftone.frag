precision mediump float;
const vec3 W = vec3(0.2125, 0.7154, 0.0721);

varying vec2 vTextureCoord;
uniform lowp sampler2D sTexture;
uniform float uPixelWidth;
uniform float uAspectRatio;

void main() {
    vec2 sampleDivisor = vec2(uPixelWidth, uPixelWidth / uAspectRatio);
    vec2 samplePos = vTextureCoord - mod(vTextureCoord, sampleDivisor) + 0.5 * sampleDivisor;
    vec2 textureCoordinateToUse = vec2(vTextureCoord.x, (vTextureCoord.y * uAspectRatio + 0.5 - 0.5 * uAspectRatio));
    vec2 adjustedSamplePos = vec2(samplePos.x, (samplePos.y * uAspectRatio + 0.5 - 0.5 * uAspectRatio));
    float distanceFromSamplePoint = distance(adjustedSamplePos, textureCoordinateToUse);
    vec3 sampledColor = texture2D(sTexture, samplePos).rgb;
    float dotScaling = 1.0 - dot(sampledColor, W);
    float checkForPresenceWithinDot = 1.0 - step(distanceFromSamplePoint, (uPixelWidth * 0.5) * dotScaling);
    gl_FragColor = vec4(vec3(checkForPresenceWithinDot), 1.0);
}