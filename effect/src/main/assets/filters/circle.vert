attribute vec4 aPosition;
attribute vec4 aTextureCoord;
varying vec2 vTextureCoord;
uniform float aspectRatio;
void main() {
    vTextureCoord = vec2(aPosition.x, aPosition.y * aspectRatio);
    gl_Position = aPosition;
}