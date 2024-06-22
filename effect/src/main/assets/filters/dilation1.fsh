precision mediump float;

uniform sampler2D sTexture;
varying vec2 vTextureCoord;

varying vec2 centerTextureCoord;
varying vec2 oneStepPositiveTextureCoord;
varying vec2 oneStepNegativeTextureCoord;


void main() {
    vec4 centerIntensity = texture2D(sTexture, centerTextureCoord);
    vec4 oneStepPositiveIntensity = texture2D(sTexture, oneStepPositiveTextureCoord);
    vec4 oneStepNegativeIntensity = texture2D(sTexture, oneStepNegativeTextureCoord);
    vec4 maxValue = max(centerIntensity, oneStepPositiveIntensity);
    gl_FragColor = max(maxValue, oneStepNegativeIntensity);
}