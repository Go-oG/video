varying highp vec2 vTextureCoord;

uniform sampler2D sTexture;

uniform mat4 uColorMatrix;
uniform float uIntensity;

void main() {
    vec4 textureColor = texture2D(sTexture, vTextureCoord);
    vec4 outputColor = textureColor * uColorMatrix;

    gl_FragColor = (uIntensity * outputColor) + ((1.0 - uIntensity) * textureColor);
}