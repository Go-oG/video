varying highp vec2 vTextureCoord;

uniform sampler2D sTexture;

uniform highp float fractionalWidthOfPixel;
uniform highp float aspectRatio;

void main() {
    highp vec2 sampleDivisor = vec2(fractionalWidthOfPixel, fractionalWidthOfPixel / aspectRatio);
    
    highp vec2 samplePos = vTextureCoord - mod(vTextureCoord, sampleDivisor) + 0.5 * sampleDivisor;
    gl_FragColor = texture2D(sTexture, samplePos );
}
