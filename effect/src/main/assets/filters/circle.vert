attribute vec4 aPosition;
attribute vec4 aTextureCoord;
varying vec2 vTextureCoord;
uniform float uAspectRatio;
void main() {
    vTextureCoord = vec2(aPosition.x, aPosition.y * uAspectRatio);
    gl_Position = aPosition;
}