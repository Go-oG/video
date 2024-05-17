varying highp vec2 vTextureCoord;
uniform sampler2D sTexture;
uniform sampler2D sTexture2;

void main() {
    lowp vec4 textureColor = texture2D(sTexture, vTextureCoord);
    lowp float greenCurveValue = texture2D(sTexture2, vec2(textureColor.g, 0.0)).g;
    
    gl_FragColor = vec4(textureColor.r, greenCurveValue, textureColor.b, textureColor.a);
}