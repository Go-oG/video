precision mediump float;

varying vec2 vTextureCoord;
uniform sampler2D sTexture;

varying vec2 leftTextureCoord;
varying vec2 topTextureCoord;
varying vec2 rightTextureCoord;
varying vec2 bottomTextureCoord;

varying vec2 topLeftTextureCoord;
varying vec2 topRightTextureCoord;
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

    vec3 resultColor = topLeftColor * 0.5 + topColor * 1.0 + topRightColor * 0.5;
    resultColor += leftColor * 1.0 + centerColor.rgb * (-6.0) + rightColor * 1.0;
    resultColor += bottomLeftColor * 0.5 + bottomColor * 1.0 + bottomRightColor * 0.5;

    // Normalize the results to allow for negative gradients in the 0.0-1.0 colorspace
    resultColor = resultColor + 0.5;

    gl_FragColor = vec4(resultColor, centerColor.a);
}