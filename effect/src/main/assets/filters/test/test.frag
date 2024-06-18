precision mediump float;
uniform sampler2D sTexture;
varying highp vec2 vTextureCoord;

uniform float uOffset;
uniform vec2 uHalfpixel;

void main() {
    vec4 sum = texture2D(sTexture, vTextureCoord + vec2(-uHalfpixel.x * 2.0, 0.0) * uOffset);
    sum += texture2D(sTexture, vTextureCoord + vec2(-uHalfpixel.x, uHalfpixel.y) * uOffset) * 2.0;
    sum += texture2D(sTexture, vTextureCoord + vec2(0.0, uHalfpixel.y * 2.0) * uOffset);
    sum += texture2D(sTexture, vTextureCoord + vec2(uHalfpixel.x, uHalfpixel.y) * uOffset) * 2.0;
    sum += texture2D(sTexture, vTextureCoord + vec2(uHalfpixel.x * 2.0, 0.0) * uOffset);
    sum += texture2D(sTexture, vTextureCoord + vec2(uHalfpixel.x, -uHalfpixel.y) * uOffset) * 2.0;
    sum += texture2D(sTexture, vTextureCoord + vec2(0.0, -uHalfpixel.y * 2.0) * uOffset);
    sum += texture2D(sTexture, vTextureCoord + vec2(-uHalfpixel.x, -uHalfpixel.y) * uOffset) * 2.0;
    //sum / 12.0
    gl_FragColor = sum * 0.0833;
}