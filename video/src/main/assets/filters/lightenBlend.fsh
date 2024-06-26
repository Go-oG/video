varying highp vec2 vTextureCoord;
varying highp vec2 vTextureCoord2;

uniform sampler2D sTexture;
uniform sampler2D sTexture2;

void main() {
   lowp vec4 textureColor = texture2D(sTexture, vTextureCoord);
   lowp vec4 textureColor2 = texture2D(sTexture2, vTextureCoord2);
   gl_FragColor = max(textureColor, textureColor2);
}