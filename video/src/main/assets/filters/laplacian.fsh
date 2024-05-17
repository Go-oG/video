precision highp float;

uniform sampler2D sTexture;

varying vec2 vTextureCoord;
varying vec2 leftTextureCoordinate;
varying vec2 rightTextureCoordinate;

varying vec2 topTextureCoordinate;
varying vec2 topLeftTextureCoordinate;
varying vec2 topRightTextureCoordinate;

varying vec2 bottomTextureCoordinate;
varying vec2 bottomLeftTextureCoordinate;
varying vec2 bottomRightTextureCoordinate;

void main()
{
    mediump vec3 bottomColor = texture2D(sTexture, bottomTextureCoordinate).rgb;
    mediump vec3 bottomLeftColor = texture2D(sTexture, bottomLeftTextureCoordinate).rgb;
    mediump vec3 bottomRightColor = texture2D(sTexture, bottomRightTextureCoordinate).rgb;
    mediump vec4 centerColor = texture2D(sTexture, vTextureCoord);
    mediump vec3 leftColor = texture2D(sTexture, leftTextureCoordinate).rgb;
    mediump vec3 rightColor = texture2D(sTexture, rightTextureCoordinate).rgb;
    mediump vec3 topColor = texture2D(sTexture, topTextureCoordinate).rgb;
    mediump vec3 topRightColor = texture2D(sTexture, topRightTextureCoordinate).rgb;
    mediump vec3 topLeftColor = texture2D(sTexture, topLeftTextureCoordinate).rgb;
    
    mediump vec3 resultColor = topLeftColor * 0.5 + topColor * 1.0 + topRightColor * 0.5;
    resultColor += leftColor * 1.0 + centerColor.rgb * (-6.0) + rightColor * 1.0;
    resultColor += bottomLeftColor * 0.5 + bottomColor * 1.0 + bottomRightColor * 0.5;
    
    // Normalize the results to allow for negative gradients in the 0.0-1.0 colorspace
    resultColor = resultColor + 0.5;
    
    gl_FragColor = vec4(resultColor, centerColor.a);
}