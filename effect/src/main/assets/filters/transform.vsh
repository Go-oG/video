attribute vec4 aPosition;
attribute vec4 aTextureCoord;

uniform mat4 transformMatrix;
uniform mat4 orthographicMatrix;

varying vec2 vTextureCoord;

void main() {
    gl_Position = transformMatrix * vec4(aPosition.xyz, 1.0) * orthographicMatrix;
    vTextureCoord = aTextureCoord.xy;
}