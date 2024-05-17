varying highp vec2 vTextureCoord;

uniform sampler2D sTexture;

uniform lowp mat4 colorMatrix;
uniform lowp float intensity;

void main()
{
    lowp vec4 textureColor = texture2D(sTexture, vTextureCoord);
    lowp vec4 outputColor = textureColor * colorMatrix;

    gl_FragColor = (intensity * outputColor) + ((1.0 - intensity) * textureColor);
}