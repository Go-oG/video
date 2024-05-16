precision highp float;

varying vec2 centerTextureCoordinate;
varying vec2 oneStepPositiveTextureCoordinate;
varying vec2 oneStepNegativeTextureCoordinate;

uniform sampler2D sTexture;

void main()
{
    lowp vec4 centerIntensity = texture2D(sTexture, centerTextureCoordinate);
    lowp vec4 oneStepPositiveIntensity = texture2D(sTexture, oneStepPositiveTextureCoordinate);
    lowp vec4 oneStepNegativeIntensity = texture2D(sTexture, oneStepNegativeTextureCoordinate);
    
    lowp vec4 minValue = min(centerIntensity, oneStepPositiveIntensity);
    
    gl_FragColor = min(minValue, oneStepNegativeIntensity);
}