precision highp float;

uniform sampler2D sTexture;

varying vec2 vTextureCoord;
varying vec2 oneStepLeftTexCoord;
varying vec2 twoStepsLeftTexCoord;
varying vec2 threeStepsLeftTexCoord;
varying vec2 fourStepsLeftTexCoord;
varying vec2 oneStepRightTexCoord;
varying vec2 twoStepsRightTexCoord;
varying vec2 threeStepsRightTexCoord;
varying vec2 fourStepsRightTexCoord;

// sinc(x) * sinc(x/a) = (a * sin(pi * x) * sin(pi * x / a)) / (pi^2 * x^2)
// Assuming a Lanczos constant of 2.0, and scaling values to max out at x = +/- 1.5

void main() {
    lowp vec4 fragmentColor = texture2D(sTexture, vTextureCoord) * 0.38026;
    
    fragmentColor += texture2D(sTexture, oneStepLeftTexCoord) * 0.27667;
    fragmentColor += texture2D(sTexture, oneStepRightTexCoord) * 0.27667;
    
    fragmentColor += texture2D(sTexture, twoStepsLeftTexCoord) * 0.08074;
    fragmentColor += texture2D(sTexture, twoStepsRightTexCoord) * 0.08074;

    fragmentColor += texture2D(sTexture, threeStepsLeftTexCoord) * -0.02612;
    fragmentColor += texture2D(sTexture, threeStepsRightTexCoord) * -0.02612;

    fragmentColor += texture2D(sTexture, fourStepsLeftTexCoord) * -0.02143;
    fragmentColor += texture2D(sTexture, fourStepsRightTexCoord) * -0.02143;

    gl_FragColor = fragmentColor;
}