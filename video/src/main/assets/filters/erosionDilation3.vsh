attribute vec4 aPosition;
attribute vec2 aTextureCoord;

uniform float texelWidth;
uniform float texelHeight;

varying vec2 centerTextureCoord;
varying vec2 oneStepPositiveTextureCoord;
varying vec2 oneStepNegativeTextureCoord;
varying vec2 twoStepsPositiveTextureCoord;
varying vec2 twoStepsNegativeTextureCoord;
varying vec2 threeStepsPositiveTextureCoord;
varying vec2 threeStepsNegativeTextureCoord;

void main()
{
    gl_Position = aPosition;
    
    vec2 offset = vec2(texelWidth, texelHeight);
    
    centerTextureCoord = aTextureCoord;
    oneStepNegativeTextureCoord = aTextureCoord - offset;
    oneStepPositiveTextureCoord = aTextureCoord + offset;
    twoStepsNegativeTextureCoord = aTextureCoord - (offset * 2.0);
    twoStepsPositiveTextureCoord = aTextureCoord + (offset * 2.0);
    threeStepsNegativeTextureCoord = aTextureCoord - (offset * 3.0);
    threeStepsPositiveTextureCoord = aTextureCoord + (offset * 3.0);
}