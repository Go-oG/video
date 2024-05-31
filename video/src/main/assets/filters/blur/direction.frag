precision mediump float;

varying vec2 vTextureCoord;
uniform sampler2D sTexture;

uniform vec2 uDirection;          // 模糊的方向，x和y分量表示方向矢量
uniform float uBlurSize;          // 模糊的大小
uniform int uSampleCount;         // 采样数量
uniform float uWeights[30];       // 权重数组
uniform float uOffsets[30];       // 偏移数组

void main() {
    vec4 sum = vec4(0.0);
    vec2 texOffset = uDirection * uBlurSize;

    for (int i = 0; i < uSampleCount; i++) {
        sum += texture2D(sTexture, vTextureCoord + uOffsets[i] * texOffset) * uWeights[i];
    }
    gl_FragColor = sum;
}
