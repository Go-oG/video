precision highp float;

varying vec2 centerTextureCoordinate;
varying vec2 oneStepPositiveTextureCoordinate;
varying vec2 oneStepNegativeTextureCoordinate;
varying vec2 twoStepsPositiveTextureCoordinate;
varying vec2 twoStepsNegativeTextureCoordinate;

uniform sampler2D sTexture;

void main()
{
    lowp vec4 centerIntensity = texture2D(sTexture, centerTextureCoordinate);
    lowp vec4 oneStepPositiveIntensity = texture2D(sTexture, oneStepPositiveTextureCoordinate);
    lowp vec4 oneStepNegativeIntensity = texture2D(sTexture, oneStepNegativeTextureCoordinate);
    lowp vec4 twoStepsPositiveIntensity = texture2D(sTexture, twoStepsPositiveTextureCoordinate);
    lowp vec4 twoStepsNegativeIntensity = texture2D(sTexture, twoStepsNegativeTextureCoordinate);
    
    lowp vec4 maxValue = max(centerIntensity, oneStepPositiveIntensity);
    maxValue = max(maxValue, oneStepNegativeIntensity);
    maxValue = max(maxValue, twoStepsPositiveIntensity);
    maxValue = max(maxValue, twoStepsNegativeIntensity);
    
    gl_FragColor = max(maxValue, twoStepsNegativeIntensity);
}