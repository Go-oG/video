precision mediump float;
varying vec2 vTextureCoord;

uniform lowp sampler2D sTexture;
uniform highp float exposure;

void main() {
    highp vec4 textureColor = texture2D(sTexture, vTextureCoord);
    gl_FragColor = vec4(textureColor.rgb * pow(2.0, exposure), textureColor.w);
}