precision mediump float;

varying vec2 vTextureCoord;
uniform sampler2D sTexture;
uniform vec2 uDirection;          // 模糊的方向，x和y分量表示方向矢量
uniform float uBlurSize;          // 模糊的大小
uniform int uSampleCount;         // 采样数量
uniform float uWeights[30];       // 权重数组
uniform float uOffsets[30];       // 偏移数组
uniform float uNoiseIntensity;    // 噪声强度

//简单的随机函数
float random(vec2 co) {
    return fract(sin(dot(co.xy, vec2(12.9898, 78.233))) * 43758.5453);
}

void main() {
    vec4 sum = vec4(0.0);
    vec2 texOffset = uDirection * uBlurSize;
    for (int i = 0; i < uSampleCount; i++) {
        // 添加噪声到偏移
        float noise = (random(vTextureCoord + uOffsets[i] * texOffset) - 0.5) * uNoiseIntensity;
        vec2 noisyOffset = texOffset * (uOffsets[i] + noise);
        sum += texture2D(sTexture, vTextureCoord + noisyOffset) * uWeights[i];
    }
    gl_FragColor = sum;
}
