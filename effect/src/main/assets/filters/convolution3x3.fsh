precision mediump float;

uniform sampler2D sTexture;
varying vec2 vTextureCoord;

uniform mediump mat3 convolutionMatrix;

varying vec2 leftTextureCoord;
varying vec2 rightTextureCoord;

varying vec2 topTextureCoord;
varying vec2 topLeftTextureCoord;
varying vec2 topRightTextureCoord;

varying vec2 bottomTextureCoord;
varying vec2 bottomLeftTextureCoord;
varying vec2 bottomRightTextureCoord;

void main() {
    vec3 bottomColor = texture2D(sTexture, bottomTextureCoord).rgb;
    vec3 bottomLeftColor = texture2D(sTexture, bottomLeftTextureCoord).rgb;
    vec3 bottomRightColor = texture2D(sTexture, bottomRightTextureCoord).rgb;
    vec4 centerColor = texture2D(sTexture, vTextureCoord);
    vec3 leftColor = texture2D(sTexture, leftTextureCoord).rgb;
    vec3 rightColor = texture2D(sTexture, rightTextureCoord).rgb;
    vec3 topColor = texture2D(sTexture, topTextureCoord).rgb;
    vec3 topRightColor = texture2D(sTexture, topRightTextureCoord).rgb;
    vec3 topLeftColor = texture2D(sTexture, topLeftTextureCoord).rgb;
    vec3 resultColor = topLeftColor * convolutionMatrix[0][0] + topColor * convolutionMatrix[0][1] + topRightColor * convolutionMatrix[0][2];
    resultColor += leftColor * convolutionMatrix[1][0] + centerColor.rgb * convolutionMatrix[1][1] + rightColor * convolutionMatrix[1][2];
    resultColor += bottomLeftColor * convolutionMatrix[2][0] + bottomColor * convolutionMatrix[2][1] + bottomRightColor * convolutionMatrix[2][2];

    gl_FragColor = vec4(resultColor, centerColor.a);
}