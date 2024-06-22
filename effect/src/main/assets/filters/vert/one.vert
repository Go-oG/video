attribute vec4 aPosition;
attribute vec4 aTextureCoord;

varying highp vec2 vTextureCoord;

void main() {
    gl_Position = aPosition;
    vTextureCoord = aTextureCoord.xy;
}