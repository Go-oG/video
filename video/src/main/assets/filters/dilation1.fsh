precision highp float;

varying vec2 centerTextureCoord;
varying vec2 oneStepPositiveTextureCoord;
varying vec2 oneStepNegativeTextureCoord;

uniform sampler2D sTexture;

void main() {
    lowp vec4 centerIntensity = texture2D(sTexture, centerTextureCoord);
    lowp vec4 oneStepPositiveIntensity = texture2D(sTexture, oneStepPositiveTextureCoord);
    lowp vec4 oneStepNegativeIntensity = texture2D(sTexture, oneStepNegativeTextureCoord);
    
    lowp vec4 maxValue = max(centerIntensity, oneStepPositiveIntensity);
    
    gl_FragColor = max(maxValue, oneStepNegativeIntensity);
}