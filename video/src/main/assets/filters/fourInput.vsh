attribute vec4 aPosition;
attribute vec4 aTextureCoord;
attribute vec4 aTextureCoord2;
attribute vec4 aTextureCoord3;
attribute vec4 aTextureCoord4;

varying vec2 vTextureCoord;
varying vec2 vTextureCoord2;
varying vec2 vTextureCoord3;
varying vec2 vTextureCoord4;

void main() {
    gl_Position = aPosition;
    vTextureCoord = aTextureCoord.xy;
    vTextureCoord2 = aTextureCoord2.xy;
    vTextureCoord3 = aTextureCoord3.xy;
    vTextureCoord4 = aTextureCoord4.xy;
}