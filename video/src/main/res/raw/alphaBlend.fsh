precision mediump float;

varying highp vec2 aTextureCoord;
varying highp vec2 aTextureCoord2;

uniform sampler2D sTexture;
uniform sampler2D sTexture2;

uniform lowp float mixturePercent;

void main() {
    lowp vec4 textureColor = texture2D(sTexture, aTextureCoord);
    lowp vec4 textureColor2 = texture2D(sTexture2, aTextureCoord2);
    gl_FragColor = vec4(mix(textureColor.rgb, textureColor2.rgb, textureColor2.a * mixturePercent), textureColor.a);
}