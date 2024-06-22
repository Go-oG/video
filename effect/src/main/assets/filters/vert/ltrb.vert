precision mediump float;

attribute vec4 aPosition;
attribute vec4 aTextureCoord;
varying highp vec2 vTextureCoord;

uniform float uTexelWidth;
uniform float uTexelHeight;

varying vec2 upperLeftCoord;
varying vec2 upperRightCoord;
varying vec2 lowerLeftCoord;
varying vec2 lowerRightCoord;

void main() {
    gl_Position = aPosition;
    vTextureCoord = aTextureCoord.xy;
    upperLeftCoord = aTextureCoord.xy + vec2(-uTexelWidth, -uTexelHeight);
    upperRightCoord = aTextureCoord.xy + vec2(uTexelWidth, -uTexelHeight);
    lowerLeftCoord = aTextureCoord.xy + vec2(-uTexelWidth, uTexelHeight);
    lowerRightCoord = aTextureCoord.xy + vec2(uTexelWidth, uTexelHeight);
}