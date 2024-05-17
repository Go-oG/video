attribute vec4 aPosition;
attribute vec2 aTextureCoord;

uniform float texelWidth;
uniform float texelHeight;

varying vec2 centerTextureCoordinate;
varying vec2 oneStepPositiveTextureCoordinate;
varying vec2 oneStepNegativeTextureCoordinate;
varying vec2 twoStepsPositiveTextureCoordinate;
varying vec2 twoStepsNegativeTextureCoordinate;
varying vec2 threeStepsPositiveTextureCoordinate;
varying vec2 threeStepsNegativeTextureCoordinate;

void main()
{
    gl_Position = aPosition;
    
    vec2 offset = vec2(texelWidth, texelHeight);
    
    centerTextureCoordinate = aTextureCoord;
    oneStepNegativeTextureCoordinate = aTextureCoord - offset;
    oneStepPositiveTextureCoordinate = aTextureCoord + offset;
    twoStepsNegativeTextureCoordinate = aTextureCoord - (offset * 2.0);
    twoStepsPositiveTextureCoordinate = aTextureCoord + (offset * 2.0);
    threeStepsNegativeTextureCoordinate = aTextureCoord - (offset * 3.0);
    threeStepsPositiveTextureCoordinate = aTextureCoord + (offset * 3.0);
}