attribute vec4 aPosition;
attribute vec2 aTextureCoord;

uniform float uPixelWidth;
uniform float uPixelHeight;

varying vec2 centerCoord;
varying vec2 oneStepPositiveCoord;
varying vec2 oneStepNegativeCoord;

void main() {
    gl_Position = aPosition;
    
    vec2 offset = vec2(uPixelWidth, uPixelHeight);
    centerCoord = aTextureCoord;
    oneStepNegativeCoord = aTextureCoord - offset;
    oneStepPositiveCoord = aTextureCoord + offset;

}