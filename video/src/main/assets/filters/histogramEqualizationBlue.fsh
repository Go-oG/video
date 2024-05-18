
varying highp vec2 vTextureCoord;

uniform sampler2D sTexture;
uniform sampler2D sTexture2;

void main() {
    vec4 textureColor = texture2D(sTexture, vTextureCoord);
    float blueCurveValue = texture2D(sTexture2, vec2(textureColor.b, 0.0)).b;
    gl_FragColor = vec4(textureColor.r, textureColor.g, blueCurveValue, textureColor.a);
}