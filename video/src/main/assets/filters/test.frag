varying highp vec2 vTextureCoord;
uniform lowp sampler2D sTexture;

uniform int uBlurRadius;
uniform float uWeights[31];
uniform float uOffsets[31];

void main() {
    float total = 0.0;
    vec3 ret = vec3(0.0);
    for (int iy = 0; iy < uBlurRadius; ++iy) {
        float offsetY = uOffsets[iy];
        float yWeight = uWeights[iy];
        for (int ix = 0; ix < uBlurRadius; ++ix) {
            float offsetX = uOffsets[ix];
            float xWeight = uWeights[ix];
            float vv = xWeight * yWeight;
            total += vv;
            ret += texture2D(sTexture, vTextureCoord + vec2(offsetX, offsetY)).rgb * vv;
        }
    }
    gl_FragColor = vec4(ret / total, 1.0);
}