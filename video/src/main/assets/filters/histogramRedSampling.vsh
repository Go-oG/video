attribute vec4 aPosition;
attribute vec4 aTextureCoord;

varying highp vec2 vTextureCoord;

varying vec3 colorFactor;

void main() {
    gl_Position = aPosition;
    vTextureCoord = aTextureCoord.xy;

    colorFactor = vec3(1.0, 0.0, 0.0);
    gl_Position = vec4(-1.0 + (aPosition.x * 0.0078125), 0.0, 0.0, 1.0);
    gl_PointSize = 1.0;
}