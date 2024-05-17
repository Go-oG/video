precision highp float;

uniform sampler2D sTexture;

varying vec2 vTextureCoord;

varying vec2 oneStepBackTextureCoordinate;
varying vec2 twoStepsBackTextureCoordinate;
varying vec2 threeStepsBackTextureCoordinate;
varying vec2 fourStepsBackTextureCoordinate;
varying vec2 oneStepForwardTextureCoordinate;
varying vec2 twoStepsForwardTextureCoordinate;
varying vec2 threeStepsForwardTextureCoordinate;
varying vec2 fourStepsForwardTextureCoordinate;

void main() {
    lowp vec4 fragmentColor = texture2D(sTexture, vTextureCoord) * 0.18;
    fragmentColor += texture2D(sTexture, oneStepBackTextureCoordinate) * 0.15;
    fragmentColor += texture2D(sTexture, twoStepsBackTextureCoordinate) *  0.12;
    fragmentColor += texture2D(sTexture, threeStepsBackTextureCoordinate) * 0.09;
    fragmentColor += texture2D(sTexture, fourStepsBackTextureCoordinate) * 0.05;
    fragmentColor += texture2D(sTexture, oneStepForwardTextureCoordinate) * 0.15;
    fragmentColor += texture2D(sTexture, twoStepsForwardTextureCoordinate) *  0.12;
    fragmentColor += texture2D(sTexture, threeStepsForwardTextureCoordinate) * 0.09;
    fragmentColor += texture2D(sTexture, fourStepsForwardTextureCoordinate) * 0.05;

    gl_FragColor = fragmentColor;
}