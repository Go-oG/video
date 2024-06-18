varying highp vec2 vTextureCoord;
varying highp vec2 vTextureCoord2;

uniform sampler2D sTexture;
uniform sampler2D sTexture2;

void main() {
    mediump vec4 base = texture2D(sTexture, vTextureCoord);
    mediump vec4 overlay = texture2D(sTexture2, vTextureCoord2);
    
    //Dca = (Sca.Da + Dca.Sa - 2.Sca.Dca) + Sca.(1 - Da) + Dca.(1 - Sa)
    
    gl_FragColor = vec4((overlay.rgb * base.a + base.rgb * overlay.a - 2.0 * overlay.rgb * base.rgb) + overlay.rgb * (1.0 - base.a) + base.rgb * (1.0 - overlay.a), base.a);
}