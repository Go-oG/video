precision highp float;

varying vec2 centerTextureCoordinate;
varying vec2 oneStepPositiveTextureCoordinate;
varying vec2 oneStepNegativeTextureCoordinate;
varying vec2 twoStepsPositiveTextureCoordinate;
varying vec2 twoStepsNegativeTextureCoordinate;
varying vec2 threeStepsPositiveTextureCoordinate;
varying vec2 threeStepsNegativeTextureCoordinate;

uniform sampler2D sTexture;

void main() {
    lowp vec4 centerIntensity = texture2D(sTexture, centerTextureCoordinate);
    lowp vec4 oneStepPositiveIntensity = texture2D(sTexture, oneStepPositiveTextureCoordinate);
    lowp vec4 oneStepNegativeIntensity = texture2D(sTexture, oneStepNegativeTextureCoordinate);
    lowp vec4 twoStepsPositiveIntensity = texture2D(sTexture, twoStepsPositiveTextureCoordinate);
    lowp vec4 twoStepsNegativeIntensity = texture2D(sTexture, twoStepsNegativeTextureCoordinate);
    lowp vec4 threeStepsPositiveIntensity = texture2D(sTexture, threeStepsPositiveTextureCoordinate);
    lowp vec4 threeStepsNegativeIntensity = texture2D(sTexture, threeStepsNegativeTextureCoordinate);
    
    lowp vec4 minValue = min(centerIntensity, oneStepPositiveIntensity);
    minValue = min(minValue, oneStepNegativeIntensity);
    minValue = min(minValue, twoStepsPositiveIntensity);
    minValue = min(minValue, twoStepsNegativeIntensity);
    minValue = min(minValue, threeStepsPositiveIntensity);
    
    gl_FragColor = min(minValue, threeStepsNegativeIntensity);
}