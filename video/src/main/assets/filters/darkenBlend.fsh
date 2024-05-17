varying highp vec2 vTextureCoord;
varying highp vec2 vTextureCoord2;

uniform sampler2D sTexture;
uniform sampler2D sTexture2;

void main() {
   lowp vec4 base = texture2D(sTexture, vTextureCoord);
   lowp vec4 overlayer = texture2D(sTexture2, vTextureCoord2);
   
   gl_FragColor = vec4(min(overlayer.rgb * base.a, base.rgb * overlayer.a) + overlayer.rgb * (1.0 - base.a) + base.rgb * (1.0 - overlayer.a), 1.0);
}