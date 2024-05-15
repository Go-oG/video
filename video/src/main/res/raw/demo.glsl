#version 100

precision mediump float;

varying highp vec2 vTextureCoord;
uniform sampler2D sTexture;
uniform int blurSize;
uniform vec2 mTexOffset;
uniform int useHorizontal;

void main() {
    vec4 color = vec4(0.0);
    float totalWeight = 0.0;
    int kernelSize = blurSize * 2 + 1;
    float sizePow = 2.0 * blurSize * blurSize;

    for (int i = -blurSize; i <= blurSize; ++i) {
        float fi = float(i);
        float weight = exp(-fi * fi / sizePow);
        vec2 offset = (useHorizontal != 0) ? vec2(fi * mTexOffset.x, 0.0) : vec2(0.0, fi * mTexOffset.y);
        color += texture(sTexture, vTextureCoord + offset) * weight;
        totalWeight += weight;
    }
    gl_FragColor = color / totalWeight;
}
