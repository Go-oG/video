precision highp float;

uniform sampler2D sTexture;
varying vec2 vTextureCoord;

varying vec2 oneStepBackTextureCoord;
varying vec2 twoStepsBackTextureCoord;
varying vec2 threeStepsBackTextureCoord;
varying vec2 fourStepsBackTextureCoord;
varying vec2 oneStepForwardTextureCoord;
varying vec2 twoStepsForwardTextureCoord;
varying vec2 threeStepsForwardTextureCoord;
varying vec2 fourStepsForwardTextureCoord;

void main() {
    lowp vec4 fragmentColor = texture2D(sTexture, vTextureCoord) * 0.18;
    fragmentColor += texture2D(sTexture, oneStepBackTextureCoord) * 0.15;
    fragmentColor += texture2D(sTexture, twoStepsBackTextureCoord) *  0.12;
    fragmentColor += texture2D(sTexture, threeStepsBackTextureCoord) * 0.09;
    fragmentColor += texture2D(sTexture, fourStepsBackTextureCoord) * 0.05;
    fragmentColor += texture2D(sTexture, oneStepForwardTextureCoord) * 0.15;
    fragmentColor += texture2D(sTexture, twoStepsForwardTextureCoord) *  0.12;
    fragmentColor += texture2D(sTexture, threeStepsForwardTextureCoord) * 0.09;
    fragmentColor += texture2D(sTexture, fourStepsForwardTextureCoord) * 0.05;

    gl_FragColor = fragmentColor;
}