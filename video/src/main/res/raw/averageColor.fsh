#version 100

precision highp float;

uniform sampler2D sTexture;
varying highp vec2 vTextureCoord;

varying highp vec2 upperLeftInputTextureCoordinate;
varying highp vec2 upperRightInputTextureCoordinate;
varying highp vec2 lowerLeftInputTextureCoordinate;
varying highp vec2 lowerRightInputTextureCoordinate;

void main() {
    highp vec4 upperLeftColor = texture2D(sTexture, upperLeftInputTextureCoordinate);
    highp vec4 upperRightColor = texture2D(sTexture, upperRightInputTextureCoordinate);
    highp vec4 lowerLeftColor = texture2D(sTexture, lowerLeftInputTextureCoordinate);
    highp vec4 lowerRightColor = texture2D(sTexture, lowerRightInputTextureCoordinate);

    gl_FragColor = 0.25 * (upperLeftColor + upperRightColor + lowerLeftColor + lowerRightColor);
}