varying highp vec2 vTextureCoord;
varying highp vec2 vTextureCoord2;

uniform sampler2D sTexture;
uniform sampler2D sTexture2;

uniform mediump mat3 colorConversionMatrix;

void main() {
    mediump vec3 yuv;
    
    yuv.x = texture2D(sTexture, vTextureCoord).r;
    yuv.yz = texture2D(sTexture2, vTextureCoord).ra - vec2(0.5, 0.5);
    lowp vec3 rgb = colorConversionMatrix * yuv;
    
    gl_FragColor = vec4(rgb, 1.0);
}