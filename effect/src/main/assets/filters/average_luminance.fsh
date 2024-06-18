precision mediump float;

uniform sampler2D sTexture;

varying mediump vec2 vTextureCoord;

varying vec2 upperLeftInputTextureCoord;
varying vec2 upperRightInputTextureCoor;
varying vec2 lowerLeftInputTextureCoord;
varying vec2 lowerRightInputTextureCoord;

void main() {
    float upperLeftLuminance = texture2D(sTexture, upperLeftInputTextureCoord).r;
    float upperRightLuminance = texture2D(sTexture, upperRightInputTextureCoor).r;
    float lowerLeftLuminance = texture2D(sTexture, lowerLeftInputTextureCoord).r;
    float lowerRightLuminance = texture2D(sTexture, lowerRightInputTextureCoord).r;
    float luminosity = 0.25 * (upperLeftLuminance + upperRightLuminance + lowerLeftLuminance + lowerRightLuminance);
    gl_FragColor = vec4(luminosity, luminosity, luminosity, 1.0);
}