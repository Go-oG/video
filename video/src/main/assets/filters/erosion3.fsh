precision highp float;

varying vec2 centerTextureCoord;
varying vec2 oneStepPositiveTextureCoord;
varying vec2 oneStepNegativeTextureCoord;
varying vec2 twoStepsPositiveTextureCoord;
varying vec2 twoStepsNegativeTextureCoord;
varying vec2 threeStepsPositiveTextureCoord;
varying vec2 threeStepsNegativeTextureCoord;

uniform sampler2D sTexture;

void main() {
    lowp vec4 centerIntensity = texture2D(sTexture, centerTextureCoord);
    lowp vec4 oneStepPositiveIntensity = texture2D(sTexture, oneStepPositiveTextureCoord);
    lowp vec4 oneStepNegativeIntensity = texture2D(sTexture, oneStepNegativeTextureCoord);
    lowp vec4 twoStepsPositiveIntensity = texture2D(sTexture, twoStepsPositiveTextureCoord);
    lowp vec4 twoStepsNegativeIntensity = texture2D(sTexture, twoStepsNegativeTextureCoord);
    lowp vec4 threeStepsPositiveIntensity = texture2D(sTexture, threeStepsPositiveTextureCoord);
    lowp vec4 threeStepsNegativeIntensity = texture2D(sTexture, threeStepsNegativeTextureCoord);
    
    lowp vec4 minValue = min(centerIntensity, oneStepPositiveIntensity);
    minValue = min(minValue, oneStepNegativeIntensity);
    minValue = min(minValue, twoStepsPositiveIntensity);
    minValue = min(minValue, twoStepsNegativeIntensity);
    minValue = min(minValue, threeStepsPositiveIntensity);
    
    gl_FragColor = min(minValue, threeStepsNegativeIntensity);
}