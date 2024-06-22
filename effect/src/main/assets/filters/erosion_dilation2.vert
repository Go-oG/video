attribute vec4 aPosition;
attribute vec2 aTextureCoord;

uniform float uPixelWidth;
uniform float uPixelHeight;

varying vec2 centerCoord;
varying vec2 oneStepPositiveCoord;
varying vec2 oneStepNegativeCoord;
varying vec2 twoStepsPositiveCoord;
varying vec2 twoStepsNegativeCoord;

void main() {
    gl_Position = aPosition;
    vec2 offset = vec2(uPixelWidth, uPixelHeight);
    centerCoord = aTextureCoord;
    oneStepNegativeCoord = aTextureCoord - offset;
    oneStepPositiveCoord = aTextureCoord + offset;
    twoStepsNegativeCoord = aTextureCoord - (offset * 2.0);
    twoStepsPositiveCoord = aTextureCoord + (offset * 2.0);
}