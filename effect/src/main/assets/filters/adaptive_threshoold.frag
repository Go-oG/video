precision mediump float;
varying vec2 vTextureCoord;
varying vec2 vTextureCoord2;

uniform sampler2D sTexture;
uniform sampler2D sTexture2;

void main() {
    float blurredInput = texture2D(sTexture, vTextureCoord).r;
    float localLuminance = texture2D(sTexture2, vTextureCoord2).r;
    float thresholdResult = step(blurredInput - 0.05, localLuminance);

    gl_FragColor = vec4(vec3(thresholdResult), 1.0);
}