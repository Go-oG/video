precision mediump float;
varying highp vec2 vTextureCoord;

uniform  sampler2D sTexture;
uniform  float opacity;

void main() {
    vec4 textureColor = texture2D(sTexture, vTextureCoord);
    gl_FragColor = textureColor * opacity;
}