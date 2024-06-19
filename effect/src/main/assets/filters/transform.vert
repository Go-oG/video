attribute vec4 aPosition;
attribute vec4 aTextureCoord;
uniform mat4 uTransformMatrix;
uniform mat4 uOrthographicMatrix;
varying vec2 vTextureCoord;

void main() {
    gl_Position = uTransformMatrix * vec4(aPosition.xyz, 1.0) * uOrthographicMatrix;
    vTextureCoord = aTextureCoord.xy;
}