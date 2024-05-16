attribute vec4 aPosition;
attribute vec2 aTextureCoord;

uniform float texelWidth; 
uniform float texelHeight; 

varying vec2 centerTextureCoordinate;
varying vec2 oneStepPositiveTextureCoordinate;
varying vec2 oneStepNegativeTextureCoordinate;

void main() {
    gl_Position = aPosition;
    
    vec2 offset = vec2(texelWidth, texelHeight);
    
    centerTextureCoordinate = aTextureCoord;
    oneStepNegativeTextureCoordinate = aTextureCoord - offset;
    oneStepPositiveTextureCoordinate = aTextureCoord + offset;
}