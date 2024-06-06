precision mediump float;

varying vec2 vTextureCoord;

varying vec2 leftTextureCoord;
varying vec2 rightTextureCoord;

varying vec2 topTextureCoord;
varying vec2 topLeftTextureCoord;
varying vec2 topRightTextureCoord;

varying vec2 bottomTextureCoord;
varying vec2 bottomLeftTextureCoord;
varying vec2 bottomRightTextureCoord;

uniform sampler2D sTexture;

void main() {
    float bottomLeftIntensity = texture2D(sTexture, bottomLeftTextureCoord).r;
    float topRightIntensity = texture2D(sTexture, topRightTextureCoord).r;
    float topLeftIntensity = texture2D(sTexture, topLeftTextureCoord).r;
    float bottomRightIntensity = texture2D(sTexture, bottomRightTextureCoord).r;
    float leftIntensity = texture2D(sTexture, leftTextureCoord).r;
    float rightIntensity = texture2D(sTexture, rightTextureCoord).r;
    float bottomIntensity = texture2D(sTexture, bottomTextureCoord).r;
    float topIntensity = texture2D(sTexture, topTextureCoord).r;
    
    vec2 gradientDirection;
    gradientDirection.x = -bottomLeftIntensity - 2.0 * leftIntensity - topLeftIntensity + bottomRightIntensity + 2.0 * rightIntensity + topRightIntensity;
    gradientDirection.y = -topLeftIntensity - 2.0 * topIntensity - topRightIntensity + bottomLeftIntensity + 2.0 * bottomIntensity + bottomRightIntensity;
    
    float gradientMagnitude = length(gradientDirection);
    vec2 normalizedDirection = normalize(gradientDirection);
    normalizedDirection = sign(normalizedDirection) * floor(abs(normalizedDirection) + 0.617316); // Offset by 1-sin(pi/8) to set to 0 if near axis, 1 if away
    normalizedDirection = (normalizedDirection + 1.0) * 0.5; // Place -1.0 - 1.0 within 0 - 1.0
    
    gl_FragColor = vec4(gradientMagnitude, normalizedDirection.x, normalizedDirection.y, 1.0);
}