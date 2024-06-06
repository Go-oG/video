uniform lowp sampler2D sTexture;
uniform int uBlurRadius;
uniform float uWeights[31];
uniform float uOffsets[31];
varying highp vec2 vTextureCoord;

void main() {
    float total = 0.0;
    vec3 ret = vec3(0.0);
    int radius = min(uBlurRadius, 31);

    // 水平模糊
    for (int i = 0; i < radius; ++i) {
        float offsetX = uOffsets[i];
        float weight = uWeights[i];
        ret += texture2D(sTexture, vTextureCoord + vec2(offsetX, 0.0)).rgb * weight;
        total += weight;
    }

    ret /= total;
    total = 0.0;
    for (int i = 0; i < radius; ++i) {
        float offsetY = uOffsets[i];
        float weight = uWeights[i];

        ret += texture2D(sTexture, vTextureCoord + vec2(0.0, offsetY)).rgb * weight;
        total += weight;
    }
    gl_FragColor = vec4(ret / total, 1.0);
}
