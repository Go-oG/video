attribute vec4 aPosition;
attribute vec4 aTextureCoord;

uniform float uCrosshairWidth;

varying vec2 vTextureCoord;
varying vec2 centerLocation;
varying float pointSpacing;

void main() {
    gl_Position = vec4(((aPosition.xy * 2.0) - 1.0), 0.0, 1.0);
    gl_PointSize = uCrosshairWidth + 1.0;
    vTextureCoord = aTextureCoord.xy;

    pointSpacing = 1.0 / uCrosshairWidth;
    centerLocation = vec2(pointSpacing * ceil(uCrosshairWidth / 2.0), pointSpacing * ceil(uCrosshairWidth / 2.0));
}