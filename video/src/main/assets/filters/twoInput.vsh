attribute vec4 aPosition;
attribute vec4 aTextureCoord;
attribute vec4 aTextureCoord2;

varying highp vec2 vTextureCoord;
varying highp vec2 vTextureCoord2;

void main() {
    gl_Position = aPosition;
    vTextureCoord = aTextureCoord.xy;
    vTextureCoord2 = aTextureCoord2.xy;
}
