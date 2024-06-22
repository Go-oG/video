precision mediump float;
varying vec2 vTextureCoord;

uniform lowp sampler2D sTexture;
uniform float uContrast;

void main() {
    vec4 textureColor = texture2D(sTexture, vTextureCoord);
    gl_FragColor = vec4(((textureColor.rgb - vec3(0.5)) * uContrast + vec3(0.5)), textureColor.w);
}