precision highp float;

varying vec2 centerTextureCoord;
varying vec2 oneStepPositiveTextureCoord;
varying vec2 oneStepNegativeTextureCoord;
varying vec2 twoStepsPositiveTextureCoord;
varying vec2 twoStepsNegativeTextureCoord;

uniform sampler2D sTexture;

void main() {
    lowp vec4 centerIntensity = texture2D(sTexture, centerTextureCoord);
    lowp vec4 oneStepPositiveIntensity = texture2D(sTexture, oneStepPositiveTextureCoord);
    lowp vec4 oneStepNegativeIntensity = texture2D(sTexture, oneStepNegativeTextureCoord);
    lowp vec4 twoStepsPositiveIntensity = texture2D(sTexture, twoStepsPositiveTextureCoord);
    lowp vec4 twoStepsNegativeIntensity = texture2D(sTexture, twoStepsNegativeTextureCoord);
    
    lowp vec4 maxValue = max(centerIntensity, oneStepPositiveIntensity);
    maxValue = max(maxValue, oneStepNegativeIntensity);
    maxValue = max(maxValue, twoStepsPositiveIntensity);
    maxValue = max(maxValue, twoStepsNegativeIntensity);
    
    gl_FragColor = max(maxValue, twoStepsNegativeIntensity);
}