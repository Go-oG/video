attribute vec4 aPosition;
attribute vec2 aTextureCoord;
varying vec2 vTextureCoord;

uniform mat4 uMVPMatrix; // 模型-视图-投影矩阵
uniform mat4 uTexMatrix; // 纹理变换矩阵

void main() {
    gl_Position = uMVPMatrix * aPosition;
    vTextureCoord = (uTexMatrix * vec4(aTextureCoord, 0.0, 1.0)).xy;
}
