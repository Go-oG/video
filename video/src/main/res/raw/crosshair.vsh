attribute vec4 aPosition;
attribute vec4 aTextureCoord;

uniform float crosshairWidth;

varying vec2 centerLocation;
varying float pointSpacing;

void main() {
    gl_Position = vec4(((aPosition.xy * 2.0) - 1.0), 0.0, 1.0);
    gl_PointSize = crosshairWidth + 1.0;
    pointSpacing = 1.0 / crosshairWidth;
    centerLocation = vec2(pointSpacing * ceil(crosshairWidth / 2.0), pointSpacing * ceil(crosshairWidth / 2.0));
}