attribute vec4 aPosition;
varying vec2 currentPosition;
uniform float aspectRatio;

void main() {
    currentPosition = vec2(position.x, position.y * aspectRatio);
    gl_Position = aPosition;
}