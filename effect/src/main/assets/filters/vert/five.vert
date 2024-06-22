attribute vec4 aPosition;
attribute vec4 aTextureCoord;
attribute vec4 aTextureCoord2;
attribute vec4 aTextureCoord3;
attribute vec4 aTextureCoord4;
attribute vec4 aTextureCoord5;

varying highp vec2 vTextureCoord;
varying highp vec2 vTextureCoord2;
varying highp vec2 vTextureCoord3;
varying highp vec2 vTextureCoord4;
varying highp vec2 vTextureCoord5;

void main() {
    gl_Position = aPosition;
    vTextureCoord = aTextureCoord.xy;
    vTextureCoord2 = aTextureCoord2.xy;
    vTextureCoord3 = aTextureCoord3.xy;
    vTextureCoord4 = aTextureCoord4.xy;
    vTextureCoord5 = aTextureCoord5.xy;
}