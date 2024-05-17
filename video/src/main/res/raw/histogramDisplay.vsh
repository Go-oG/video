attribute vec4 aPosition;
attribute vec4 aTextureCoord;

varying vec2 vTextureCoord;
varying float height;

void main() {
    gl_Position = aPosition;
    vTextureCoord = vec2(aTextureCoord.x, 0.5);
    height = 1.0 - aTextureCoord.y;
}