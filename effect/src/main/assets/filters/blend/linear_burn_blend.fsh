varying highp vec2 vTextureCoord;
varying highp vec2 vTextureCoord2;

uniform sampler2D sTexture;
uniform sampler2D sTexture2;

void main() {
    mediump vec4 textureColor = texture2D(sTexture, vTextureCoord);
    mediump vec4 textureColor2 = texture2D(sTexture2, vTextureCoord2);
    
    gl_FragColor = vec4(clamp(textureColor.rgb + textureColor2.rgb - vec3(1.0), vec3(0.0), vec3(1.0)), textureColor.a);
}