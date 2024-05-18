attribute vec4 aPosition;
attribute vec4 aTextureCoord;

uniform vec2 directionalTexelStep;

varying vec2 vTextureCoord;
varying vec2 oneStepBackTextureCoord;
varying vec2 twoStepsBackTextureCoord;
varying vec2 threeStepsBackTextureCoord;
varying vec2 fourStepsBackTextureCoord;
varying vec2 oneStepForwardTextureCoord;
varying vec2 twoStepsForwardTextureCoord;
varying vec2 threeStepsForwardTextureCoord;
varying vec2 fourStepsForwardTextureCoord;

void main() {
    gl_Position = aPosition;
    vTextureCoord = aTextureCoord.xy;

    oneStepBackTextureCoord = aTextureCoord.xy - directionalTexelStep;
    twoStepsBackTextureCoord = aTextureCoord.xy - 2.0 * directionalTexelStep;
    threeStepsBackTextureCoord = aTextureCoord.xy - 3.0 * directionalTexelStep;
    fourStepsBackTextureCoord = aTextureCoord.xy - 4.0 * directionalTexelStep;
    oneStepForwardTextureCoord = aTextureCoord.xy + directionalTexelStep;
    twoStepsForwardTextureCoord = aTextureCoord.xy + 2.0 * directionalTexelStep;
    threeStepsForwardTextureCoord = aTextureCoord.xy + 3.0 * directionalTexelStep;
    fourStepsForwardTextureCoord = aTextureCoord.xy + 4.0 * directionalTexelStep;
}