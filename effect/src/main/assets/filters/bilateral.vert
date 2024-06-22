attribute vec4 aPosition;
attribute vec4 aTextureCoord;

const int GAUSSIAN_SAMPLES = 9;

varying vec2 vTextureCoord;
varying vec2 blurCoordinates[GAUSSIAN_SAMPLES];

uniform float uPixelWidth;
uniform float uPixelHeight;
uniform float uBlurSize;


void main() {
    gl_Position = aPosition;
    vTextureCoord = aTextureCoord.xy;
    int multiplier = 0;
    vec2 blurStep;
    vec2 singleStepOffset = vec2(uPixelHeight, uPixelWidth) * uBlurSize;
    for (int i = 0; i < GAUSSIAN_SAMPLES; i++) {
        multiplier = (i - ((GAUSSIAN_SAMPLES - 1) / 2));
        blurStep = float(multiplier) * singleStepOffset;
        blurCoordinates[i] = vTextureCoord.xy + blurStep;
    }
}