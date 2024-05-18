precision highp float;

uniform sampler2D sTexture;
varying highp vec2 vTextureCoord;


varying highp vec2 upperLeftInputTextureCoord;
varying highp vec2 upperRightInputTextureCoord;
varying highp vec2 lowerLeftInputTextureCoord;
varying highp vec2 lowerRightInputTextureCoord;

void main() {
    highp vec4 upperLeftColor = texture2D(sTexture, upperLeftInputTextureCoord);
    highp vec4 upperRightColor = texture2D(sTexture, upperRightInputTextureCoord);
    highp vec4 lowerLeftColor = texture2D(sTexture, lowerLeftInputTextureCoord);
    highp vec4 lowerRightColor = texture2D(sTexture, lowerRightInputTextureCoord);

    gl_FragColor = 0.25 * (upperLeftColor + upperRightColor + lowerLeftColor + lowerRightColor);
}