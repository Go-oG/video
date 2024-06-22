precision mediump float;

uniform sampler2D sTexture;
varying vec2 centerCoord;
varying vec2 oneStepPositiveCoord;
varying vec2 oneStepNegativeCoord;
varying vec2 twoStepsPositiveCoord;
varying vec2 twoStepsNegativeCoord;
varying vec2 threeStepsPositiveCoord;
varying vec2 threeStepsNegativeCoord;
varying vec2 fourStepsPositiveCoord;
varying vec2 fourStepsNegativeCoord;


void main() {
    vec4 centerIntensity = texture2D(sTexture, centerCoord);
    vec4 oneStepPositiveIntensity = texture2D(sTexture, oneStepPositiveCoord);
    vec4 oneStepNegativeIntensity = texture2D(sTexture, oneStepNegativeCoord);
    vec4 twoStepsPositiveIntensity = texture2D(sTexture, twoStepsPositiveCoord);
    vec4 twoStepsNegativeIntensity = texture2D(sTexture, twoStepsNegativeCoord);
    vec4 threeStepsPositiveIntensity = texture2D(sTexture, threeStepsPositiveCoord);
    vec4 threeStepsNegativeIntensity = texture2D(sTexture, threeStepsNegativeCoord);
    vec4 fourStepsPositiveIntensity = texture2D(sTexture, fourStepsPositiveCoord);
    vec4 fourStepsNegativeIntensity = texture2D(sTexture, fourStepsNegativeCoord);

    vec4 minValue = min(centerIntensity, oneStepPositiveIntensity);
    minValue = min(minValue, oneStepNegativeIntensity);
    minValue = min(minValue, twoStepsPositiveIntensity);
    minValue = min(minValue, twoStepsNegativeIntensity);
    minValue = min(minValue, threeStepsPositiveIntensity);
    minValue = min(minValue, threeStepsNegativeIntensity);
    minValue = min(minValue, fourStepsPositiveIntensity);
    
    gl_FragColor = min(minValue, fourStepsNegativeIntensity);
}