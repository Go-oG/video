varying highp vec2 vTextureCoord;
varying highp vec2 vTextureCoord;

uniform sampler2D sTexture;
uniform sampler2D sTexture2;

void main()
{
    mediump vec4 textureColor = texture2D(sTexture, vTextureCoord);
    mediump vec4 textureColor2 = texture2D(sTexture2, vTextureCoord2);
    mediump vec4 whiteColor = vec4(1.0);
    gl_FragColor = whiteColor - (whiteColor - textureColor) / textureColor2;
}