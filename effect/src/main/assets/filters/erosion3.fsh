precision mediump float;

varying vec2 centerCoord;
varying vec2 oneStepPositiveCoord;
varying vec2 oneStepNegativeCoord;
varying vec2 twoStepsPositiveCoord;
varying vec2 twoStepsNegativeCoord;
varying vec2 threeStepsPositiveCoord;
varying vec2 threeStepsNegativeCoord;

uniform sampler2D sTexture;

void main() {
    mediump vec4 centerIntensity = texture2D(sTexture, centerCoord);
    mediump vec4 oneStepPositiveIntensity = texture2D(sTexture, oneStepPositiveCoord);
    mediump vec4 oneStepNegativeIntensity = texture2D(sTexture, oneStepNegativeCoord);
    mediump vec4 twoStepsPositiveIntensity = texture2D(sTexture, twoStepsPositiveCoord);
    mediump vec4 twoStepsNegativeIntensity = texture2D(sTexture, twoStepsNegativeCoord);
    mediump vec4 threeStepsPositiveIntensity = texture2D(sTexture, threeStepsPositiveCoord);
    mediump vec4 threeStepsNegativeIntensity = texture2D(sTexture, threeStepsNegativeCoord);
    mediump vec4 minValue = min(centerIntensity, oneStepPositiveIntensity);
    minValue = min(minValue, oneStepNegativeIntensity);
    minValue = min(minValue, twoStepsPositiveIntensity);
    minValue = min(minValue, twoStepsNegativeIntensity);
    minValue = min(minValue, threeStepsPositiveIntensity);
    
    gl_FragColor = min(minValue, threeStepsNegativeIntensity);
}