varying highp vec2 vTextureCoord;
uniform lowp sampler2D sTexture;

uniform int uBlurRadius;
uniform float sTextureSize;

void computeWeights(int blurSize){


}


void main() {
    float mSigmaX = 5.0;
    float mSigmaY = mSigmaX;
    int halfSamples = uBlurRadius / 2;
    float mSigmaX2 = 2.0 * mSigmaX * mSigmaX;
    float mPixelSize = 1.0 / sTextureSize;

    float total = 0.0;
    vec3 ret = vec3(0.0);

    for (int iy = 0; iy < uBlurRadius; ++iy) {
        float offsetY = float(iy - halfSamples);
        float fy = exp(-offsetY * offsetY / mSigmaX2);
        offsetY = offsetY * mPixelSize;
        for (int ix = 0; ix < uBlurRadius; ++ix) {
            float offsetX = float(ix - halfSamples);
            float fx = exp(-offsetX * offsetX / mSigmaX2);

            offsetX = offsetX * mPixelSize;
            total += fx * fy;
            ret += texture2D(sTexture, vTextureCoord + vec2(offsetX, offsetY)).rgb * fx * fy;
        }
    }
    gl_FragColor = vec4(ret / total, 1.0);
}