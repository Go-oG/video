attribute vec4 aPosition;
attribute vec2 aTextureCoord;

uniform float uPixelWidth;
uniform float uPixelHeight;

varying vec2 centerTextureCoord;
varying vec2 oneStepPositiveCoord;
varying vec2 oneStepNegativeCoord;
varying vec2 twoStepsPositiveCoord;
varying vec2 twoStepsNegativeCoord;
varying vec2 threeStepsPositiveCoord;
varying vec2 threeStepsNegativeCoord;

void main() {
    gl_Position = aPosition;
    vec2 offset = vec2(uPixelWidth, uPixelHeight);
    centerTextureCoord = aTextureCoord;
    oneStepNegativeCoord = aTextureCoord - offset;
    oneStepPositiveCoord = aTextureCoord + offset;
    twoStepsNegativeCoord = aTextureCoord - (offset * 2.0);
    twoStepsPositiveCoord = aTextureCoord + (offset * 2.0);
    threeStepsNegativeCoord = aTextureCoord - (offset * 3.0);
    threeStepsPositiveCoord = aTextureCoord + (offset * 3.0);
}