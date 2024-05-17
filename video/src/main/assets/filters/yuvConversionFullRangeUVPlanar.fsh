varying highp vec2 vTextureCoord;
varying highp vec2 vTextureCoord2;
varying highp vec2 vTextureCoord3;

uniform sampler2D sTexture;
uniform sampler2D sTexture2;
uniform sampler2D sTexture3;

uniform mediump mat3 colorConversionMatrix;

void main() {
    mediump vec3 yuv;
    
    yuv.x = texture2D(sTexture, vTextureCoord).r;
    yuv.y = texture2D(sTexture2, vTextureCoord).r - 0.5;
    yuv.z = texture2D(sTexture3, vTextureCoord).r - 0.5;
    lowp vec3 rgb = colorConversionMatrix * yuv;
    
    gl_FragColor = vec4(rgb, 1.0);
}