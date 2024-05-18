attribute vec4 aPosition;

varying vec2 currentPosition;

uniform float aspectRatio;

void main() {
    currentPosition = vec2(aPosition.x, aPosition.y * aspectRatio);
    gl_Position = aPosition;
}