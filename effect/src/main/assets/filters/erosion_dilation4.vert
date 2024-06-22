attribute vec4 aPosition;
attribute vec2 aTextureCoord;

uniform float uPixelWidth;
uniform float uPixelHeight;

varying vec2 centerCoord;
varying vec2 oneStepPositiveCoord;
varying vec2 oneStepNegativeCoord;
varying vec2 twoStepsPositiveCoord;
varying vec2 twoStepsNegativeCoord;
varying vec2 threeStepsPositiveCoord;
varying vec2 threeStepsNegativeCoord;
varying vec2 fourStepsPositiveCoord;
varying vec2 fourStepsNegativeCoord;

void main() {
    gl_Position = aPosition;
    centerCoord = aTextureCoord;

    vec2 offset = vec2(uPixelWidth, uPixelHeight);
    oneStepNegativeCoord = aTextureCoord - offset;
    oneStepPositiveCoord = aTextureCoord + offset;
    twoStepsNegativeCoord = aTextureCoord - (offset * 2.0);
    twoStepsPositiveCoord = aTextureCoord + (offset * 2.0);
    threeStepsNegativeCoord = aTextureCoord - (offset * 3.0);
    threeStepsPositiveCoord = aTextureCoord + (offset * 3.0);
    fourStepsNegativeCoord = aTextureCoord - (offset * 4.0);
    fourStepsPositiveCoord = aTextureCoord + (offset * 4.0);
}