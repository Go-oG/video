attribute vec4 aPosition;
attribute vec4 aTextureCoord;

uniform vec2 directionalTexelStep;

varying vec2 vTextureCoord;
varying vec2 oneStepBackTextureCoordinate;
varying vec2 twoStepsBackTextureCoordinate;
varying vec2 threeStepsBackTextureCoordinate;
varying vec2 fourStepsBackTextureCoordinate;
varying vec2 oneStepForwardTextureCoordinate;
varying vec2 twoStepsForwardTextureCoordinate;
varying vec2 threeStepsForwardTextureCoordinate;
varying vec2 fourStepsForwardTextureCoordinate;

void main() {
    gl_Position = aPosition;

    vTextureCoord = aTextureCoord.xy;
    oneStepBackTextureCoordinate = aTextureCoord.xy - directionalTexelStep;
    twoStepsBackTextureCoordinate = aTextureCoord.xy - 2.0 * directionalTexelStep;
    threeStepsBackTextureCoordinate = aTextureCoord.xy - 3.0 * directionalTexelStep;
    fourStepsBackTextureCoordinate = aTextureCoord.xy - 4.0 * directionalTexelStep;
    oneStepForwardTextureCoordinate = aTextureCoord.xy + directionalTexelStep;
    twoStepsForwardTextureCoordinate = aTextureCoord.xy + 2.0 * directionalTexelStep;
    threeStepsForwardTextureCoordinate = aTextureCoord.xy + 3.0 * directionalTexelStep;
    fourStepsForwardTextureCoordinate = aTextureCoord.xy + 4.0 * directionalTexelStep;
}