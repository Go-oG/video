varying highp vec2 vTextureCoord;
uniform sampler2D sTexture;
uniform sampler2D sTexture2;

void main() {
    lowp vec4 textureColor = texture2D(sTexture, vTextureCoord);
    lowp float redCurveValue = texture2D(sTexture2, vec2(textureColor.r, 0.0)).r;
    
    gl_FragColor = vec4(redCurveValue, textureColor.g, textureColor.b, textureColor.a);
}