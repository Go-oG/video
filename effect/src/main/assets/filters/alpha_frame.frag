precision mediump float;
varying highp vec2 vTextureCoord;
varying highp vec2 vTextureCoord2;
uniform lowp sampler2D sTexture;
void main() {
    vec4 color1 = texture2D(sTexture, vTextureCoord);
    vec4 color2 = texture2D(sTexture, vTextureCoord2);
    gl_FragColor = vec4(color1.rgb, color2.r);
}