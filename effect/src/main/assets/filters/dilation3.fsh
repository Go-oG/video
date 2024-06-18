precision mediump float;

varying vec2 centerTextureCoord;
varying vec2 oneStepPositiveTextureCoord;
varying vec2 oneStepNegativeTextureCoord;
varying vec2 twoStepsPositiveTextureCoord;
varying vec2 twoStepsNegativeTextureCoord;
varying vec2 threeStepsPositiveTextureCoord;
varying vec2 threeStepsNegativeTextureCoord;

uniform sampler2D sTexture;

void main() {
    vec4 centerIntensity = texture2D(sTexture, centerTextureCoord);
    vec4 oneStepPositiveIntensity = texture2D(sTexture, oneStepPositiveTextureCoord);
    vec4 oneStepNegativeIntensity = texture2D(sTexture, oneStepNegativeTextureCoord);
    vec4 twoStepsPositiveIntensity = texture2D(sTexture, twoStepsPositiveTextureCoord);
    vec4 twoStepsNegativeIntensity = texture2D(sTexture, twoStepsNegativeTextureCoord);
    vec4 threeStepsPositiveIntensity = texture2D(sTexture, threeStepsPositiveTextureCoord);
    vec4 threeStepsNegativeIntensity = texture2D(sTexture, threeStepsNegativeTextureCoord);

    vec4 maxValue = max(centerIntensity, oneStepPositiveIntensity);
    maxValue = max(maxValue, oneStepNegativeIntensity);
    maxValue = max(maxValue, twoStepsPositiveIntensity);
    maxValue = max(maxValue, twoStepsNegativeIntensity);
    maxValue = max(maxValue, threeStepsPositiveIntensity);
    
    gl_FragColor = max(maxValue, threeStepsNegativeIntensity);
}