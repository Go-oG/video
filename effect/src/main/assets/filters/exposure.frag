precision mediump float;
varying vec2 vTextureCoord;

uniform lowp sampler2D sTexture;
uniform float uExposure;

void main() {
    vec4 textureColor = texture2D(sTexture, vTextureCoord);
    gl_FragColor = vec4(textureColor.rgb * pow(2.0, uExposure), textureColor.w);
}