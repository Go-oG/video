precision mediump float;

varying vec2 centerCoord;
varying vec2 oneStepPositiveCoord;
varying vec2 oneStepNegativeCoord;

uniform sampler2D sTexture;

void main() {
    vec4 centerIntensity = texture2D(sTexture, centerCoord);
    vec4 oneStepPositiveIntensity = texture2D(sTexture, oneStepPositiveCoord);
    vec4 oneStepNegativeIntensity = texture2D(sTexture, oneStepNegativeCoord);

    vec4 minValue = min(centerIntensity, oneStepPositiveIntensity);
    
    gl_FragColor = min(minValue, oneStepNegativeIntensity);
}