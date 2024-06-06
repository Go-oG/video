attribute vec4 aPosition;
attribute vec4 aTextureCoord;
attribute vec4 aTextureCoord2;
attribute vec4 aTextureCoord3;
attribute vec4 aTextureCoord4;
attribute vec4 aTextureCoord5;
attribute vec4 aTextureCoord6;
attribute vec4 aTextureCoord7;
attribute vec4 aTextureCoord8;
attribute vec4 aTextureCoord9;

varying highp vec2 vTextureCoord;
varying highp vec2 vTextureCoord2;
varying highp vec2 vTextureCoord3;
varying highp vec2 vTextureCoord4;
varying highp vec2 vTextureCoord5;
varying highp vec2 vTextureCoord6;
varying highp vec2 vTextureCoord7;
varying highp vec2 vTextureCoord8;
varying highp vec2 vTextureCoord9;

void main() {
    gl_Position = aPosition;
    vTextureCoord = aTextureCoord.xy;
    vTextureCoord2 = aTextureCoord2.xy;
    vTextureCoord3 = aTextureCoord3.xy;
    vTextureCoord4 = aTextureCoord4.xy;
    vTextureCoord5 = aTextureCoord5.xy;
    vTextureCoord6 = aTextureCoord6.xy;
    vTextureCoord7 = aTextureCoord7.xy;
    vTextureCoord8 = aTextureCoord8.xy;
    vTextureCoord9 = aTextureCoord9.xy;
}