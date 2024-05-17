varying highp vec2 vTextureCoord;
varying highp vec2 vTextureCoord2;

uniform sampler2D sTexture;
uniform sampler2D sTexture2;

uniform highp float intensity;

void main() {
    lowp vec4 sharpImageColor = texture2D(sTexture, vTextureCoord);
    lowp vec3 blurredImageColor = texture2D(sTexture2, vTextureCoord2).rgb;
    
    gl_FragColor = vec4(sharpImageColor.rgb * intensity + blurredImageColor * (1.0 - intensity), sharpImageColor.a);
}
