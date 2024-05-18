precision highp float;

uniform sampler2D sTexture;

varying highp vec2 vTextureCoord;

varying highp vec2 upperLeftInputTextureCoord;
varying highp vec2 upperRightInputTextureCoor;
varying highp vec2 lowerLeftInputTextureCoord;
varying highp vec2 lowerRightInputTextureCoord;

void main() {
    highp float upperLeftLuminance = texture2D(sTexture, upperLeftInputTextureCoord).r;
    highp float upperRightLuminance = texture2D(sTexture, upperRightInputTextureCoor).r;
    highp float lowerLeftLuminance = texture2D(sTexture, lowerLeftInputTextureCoord).r;
    highp float lowerRightLuminance = texture2D(sTexture, lowerRightInputTextureCoord).r;
    highp float luminosity = 0.25 * (upperLeftLuminance + upperRightLuminance + lowerLeftLuminance + lowerRightLuminance);
    gl_FragColor = vec4(luminosity, luminosity, luminosity, 1.0);
}