attribute vec4 aPosition;
attribute vec4 aTextureCoord;

varying vec2 currentPosition;

uniform float aspectRatio;

void main() {
    currentPosition = vec2(aPosition.x, aPosition.y * aspectRatio);
    gl_Position = aPosition;
}