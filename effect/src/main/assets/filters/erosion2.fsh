precision mediump float;

varying vec2 centerCoord;
varying vec2 oneStepPositiveCoord;
varying vec2 oneStepNegativeCoord;
varying vec2 twoStepsPositiveCoord;
varying vec2 twoStepsNegativeCoord;

uniform sampler2D sTexture;

void main() {
    vec4 centerIntensity = texture2D(sTexture, centerCoord);
    vec4 oneStepPositiveIntensity = texture2D(sTexture, oneStepPositiveCoord);
    vec4 oneStepNegativeIntensity = texture2D(sTexture, oneStepNegativeCoord);
    vec4 twoStepsPositiveIntensity = texture2D(sTexture, twoStepsPositiveCoord);
    vec4 twoStepsNegativeIntensity = texture2D(sTexture, twoStepsNegativeCoord);
    vec4 minValue = min(centerIntensity, oneStepPositiveIntensity);
    minValue = min(minValue, oneStepNegativeIntensity);
    minValue = min(minValue, twoStepsPositiveIntensity);
    
    gl_FragColor = min(minValue, twoStepsNegativeIntensity);
}