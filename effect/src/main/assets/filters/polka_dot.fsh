varying highp vec2 vTextureCoord;

uniform sampler2D sTexture;

uniform highp float fractionalWidthOfPixel;
uniform highp float aspectRatio;
uniform highp float dotScaling;

void main() {
    highp vec2 sampleDivisor = vec2(fractionalWidthOfPixel, fractionalWidthOfPixel / aspectRatio);
    
    highp vec2 samplePos = vTextureCoord - mod(vTextureCoord, sampleDivisor) + 0.5 * sampleDivisor;
    highp vec2 textureCoordinateToUse = vec2(vTextureCoord.x, (vTextureCoord.y * aspectRatio + 0.5 - 0.5 * aspectRatio));
    highp vec2 adjustedSamplePos = vec2(samplePos.x, (samplePos.y * aspectRatio + 0.5 - 0.5 * aspectRatio));
    highp float distanceFromSamplePoint = distance(adjustedSamplePos, textureCoordinateToUse);
    lowp float checkForPresenceWithinDot = step(distanceFromSamplePoint, (fractionalWidthOfPixel * 0.5) * dotScaling);

    lowp vec4 inputColor = texture2D(sTexture, samplePos);
    
    gl_FragColor = vec4(inputColor.rgb * checkForPresenceWithinDot, inputColor.a);
}