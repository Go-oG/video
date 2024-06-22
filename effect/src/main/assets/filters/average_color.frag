precision mediump float;
varying mediump vec2 vTextureCoord;
uniform sampler2D sTexture;
varying vec2 upperLeftCoord;
varying vec2 upperRightCoord;
varying vec2 lowerLeftCoord;
varying vec2 lowerRightCoord;

void main() {
    vec4 upperLeftColor = texture2D(sTexture, upperLeftCoord);
    vec4 upperRightColor = texture2D(sTexture, upperRightCoord);
    vec4 lowerLeftColor = texture2D(sTexture, lowerLeftCoord);
    vec4 lowerRightColor = texture2D(sTexture, lowerRightCoord);
    gl_FragColor = 0.25 * (upperLeftColor + upperRightColor + lowerLeftColor + lowerRightColor);
}