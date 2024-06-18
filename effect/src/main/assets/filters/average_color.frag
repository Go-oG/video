precision mediump float;
varying mediump vec2 vTextureCoord;
uniform sampler2D sTexture;
varying  vec2 upperLeftInputTextureCoord;
varying  vec2 upperRightInputTextureCoord;
varying  vec2 lowerLeftInputTextureCoord;
varying  vec2 lowerRightInputTextureCoord;

void main() {
    vec4 upperLeftColor = texture2D(sTexture, upperLeftInputTextureCoord);
    vec4 upperRightColor = texture2D(sTexture, upperRightInputTextureCoord);
    vec4 lowerLeftColor = texture2D(sTexture, lowerLeftInputTextureCoord);
    vec4 lowerRightColor = texture2D(sTexture, lowerRightInputTextureCoord);
    gl_FragColor = 0.25 * (upperLeftColor + upperRightColor + lowerLeftColor + lowerRightColor);
}