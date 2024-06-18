precision mediump float;

uniform sampler2D sTexture;
varying vec2 centerTextureCoord;
varying vec2 oneStepPositiveTextureCoord;
varying vec2 oneStepNegativeTextureCoord;
varying vec2 twoStepsPositiveTextureCoord;
varying vec2 twoStepsNegativeTextureCoord;
varying vec2 threeStepsPositiveTextureCoord;
varying vec2 threeStepsNegativeTextureCoord;
varying vec2 fourStepsPositiveTextureCoord;
varying vec2 fourStepsNegativeTextureCoord;


void main() {
    vec4 centerIntensity = texture2D(sTexture, centerTextureCoord);
    vec4 oneStepPositiveIntensity = texture2D(sTexture, oneStepPositiveTextureCoord);
    vec4 oneStepNegativeIntensity = texture2D(sTexture, oneStepNegativeTextureCoord);
    vec4 twoStepsPositiveIntensity = texture2D(sTexture, twoStepsPositiveTextureCoord);
    vec4 twoStepsNegativeIntensity = texture2D(sTexture, twoStepsNegativeTextureCoord);
    vec4 threeStepsPositiveIntensity = texture2D(sTexture, threeStepsPositiveTextureCoord);
    vec4 threeStepsNegativeIntensity = texture2D(sTexture, threeStepsNegativeTextureCoord);
    vec4 fourStepsPositiveIntensity = texture2D(sTexture, fourStepsPositiveTextureCoord);
    vec4 fourStepsNegativeIntensity = texture2D(sTexture, fourStepsNegativeTextureCoord);

    vec4 minValue = min(centerIntensity, oneStepPositiveIntensity);
    minValue = min(minValue, oneStepNegativeIntensity);
    minValue = min(minValue, twoStepsPositiveIntensity);
    minValue = min(minValue, twoStepsNegativeIntensity);
    minValue = min(minValue, threeStepsPositiveIntensity);
    minValue = min(minValue, threeStepsNegativeIntensity);
    minValue = min(minValue, fourStepsPositiveIntensity);
    
    gl_FragColor = min(minValue, fourStepsNegativeIntensity);
}