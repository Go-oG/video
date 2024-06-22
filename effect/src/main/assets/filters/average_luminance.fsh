precision mediump float;

uniform sampler2D sTexture;

varying mediump vec2 vTextureCoord;

varying vec2 upperLeftCoord;
varying vec2 upperRightCoord;
varying vec2 lowerLeftCoord;
varying vec2 lowerRightCoord;

void main() {
    float upperLeft = texture2D(sTexture, upperLeftCoord).r;
    float upperRight = texture2D(sTexture, upperRightCoord).r;
    float lowerLeft = texture2D(sTexture, lowerLeftCoord).r;
    float lowerRight = texture2D(sTexture, lowerRightCoord).r;
    float luminosity = 0.25 * (upperLeft + upperRight + lowerLeft + lowerRight);
    gl_FragColor = vec4(luminosity, luminosity, luminosity, 1.0);
}