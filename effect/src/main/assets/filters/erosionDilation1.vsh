attribute vec4 aPosition;
attribute vec2 aTextureCoord;

uniform float texelWidth; 
uniform float texelHeight; 

varying vec2 centerTextureCoord;
varying vec2 oneStepPositiveTextureCoord;
varying vec2 oneStepNegativeTextureCoord;

void main() {
    gl_Position = aPosition;
    
    vec2 offset = vec2(texelWidth, texelHeight);
    
    centerTextureCoord = aTextureCoord;
    oneStepNegativeTextureCoord = aTextureCoord - offset;
    oneStepPositiveTextureCoord = aTextureCoord + offset;
}