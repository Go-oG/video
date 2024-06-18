attribute vec4 aPosition;
attribute vec4 aTextureCoord;
varying highp vec2 vTextureCoord;
varying highp vec2 vTextureCoord2;
void main() {
    gl_Position = aPosition;
    vTextureCoord = vec2(aTextureCoord.x, aTextureCoord.y * 0.5 + 0.5);
    vTextureCoord2 = vec2(aTextureCoord.x, aTextureCoord.y * 0.5);
}