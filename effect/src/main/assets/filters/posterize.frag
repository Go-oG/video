precision mediump float;
varying vec2 vTextureCoord;

uniform lowp sampler2D sTexture;
uniform highp float colorLevels;

void main() {
    highp vec4 textureColor = texture2D(sTexture, vTextureCoord);
    gl_FragColor = floor((textureColor * colorLevels) + vec4(0.5)) / colorLevels;
}