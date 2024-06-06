attribute vec4 aPosition;
attribute vec4 aTextureCoord;
varying highp vec2 vTextureCoord;
uniform float texelWidth;
uniform float texelHeight;

varying vec2 upperLeftInputTextureCoord;
varying vec2 upperRightInputTextureCoord;
varying vec2 lowerLeftInputTextureCoord;
varying vec2 lowerRightInputTextureCoord;

void main() {
    gl_Position = aPosition;
    vTextureCoord = aTextureCoord.xy;
    upperLeftInputTextureCoord = aTextureCoord.xy + vec2(-texelWidth, -texelHeight);
    upperRightInputTextureCoord = aTextureCoord.xy + vec2(texelWidth, -texelHeight);
    lowerLeftInputTextureCoord = aTextureCoord.xy + vec2(-texelWidth, texelHeight);
    lowerRightInputTextureCoord = aTextureCoord.xy + vec2(texelWidth, texelHeight);
}