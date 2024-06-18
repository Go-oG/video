precision mediump float;

varying highp vec2 vTextureCoord;
uniform sampler2D sTexture;

uniform float uWidthFactor;
uniform float uAspectRatio;

void main() {
    vec2 sampleDivisor = vec2(uWidthFactor, uWidthFactor / uAspectRatio);
    vec2 samplePos = vTextureCoord - mod(vTextureCoord, sampleDivisor) + 0.5 * sampleDivisor;
    gl_FragColor = texture2D(sTexture, samplePos);
}