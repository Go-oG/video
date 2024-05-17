precision highp float;

uniform sampler2D sTexture;

varying highp vec2 vTextureCoord;

varying highp vec2 upperLeftInputTextureCoordinate;
varying highp vec2 upperRightInputTextureCoordinate;
varying highp vec2 lowerLeftInputTextureCoordinate;
varying highp vec2 lowerRightInputTextureCoordinate;

void main() {
    highp float upperLeftLuminance = texture2D(sTexture, upperLeftInputTextureCoordinate).r;
    highp float upperRightLuminance = texture2D(sTexture, upperRightInputTextureCoordinate).r;
    highp float lowerLeftLuminance = texture2D(sTexture, lowerLeftInputTextureCoordinate).r;
    highp float lowerRightLuminance = texture2D(sTexture, lowerRightInputTextureCoordinate).r;

    highp float luminosity = 0.25 * (upperLeftLuminance + upperRightLuminance + lowerLeftLuminance + lowerRightLuminance);
    gl_FragColor = vec4(luminosity, luminosity, luminosity, 1.0);
}