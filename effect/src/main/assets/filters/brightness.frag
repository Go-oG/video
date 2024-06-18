precision mediump float;
varying vec2 vTextureCoord;

uniform lowp sampler2D sTexture;
uniform  float brightness;

void main() {
    vec4 textureColor = texture2D(sTexture, vTextureCoord);
    gl_FragColor = vec4((textureColor.rgb + vec3(brightness)), textureColor.w);
}