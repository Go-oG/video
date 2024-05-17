precision mediump float;

varying vec2 vTextureCoord;

varying vec2 leftTextureCoordinate;
varying vec2 rightTextureCoordinate;

varying vec2 topTextureCoordinate;
varying vec2 topLeftTextureCoordinate;
varying vec2 topRightTextureCoordinate;

varying vec2 bottomTextureCoordinate;
varying vec2 bottomLeftTextureCoordinate;
varying vec2 bottomRightTextureCoordinate;

uniform sampler2D sTexture;

void main() {
    float bottomLeftIntensity = texture2D(sTexture, bottomLeftTextureCoordinate).r;
    float topRightIntensity = texture2D(sTexture, topRightTextureCoordinate).r;
    float topLeftIntensity = texture2D(sTexture, topLeftTextureCoordinate).r;
    float bottomRightIntensity = texture2D(sTexture, bottomRightTextureCoordinate).r;
    float leftIntensity = texture2D(sTexture, leftTextureCoordinate).r;
    float rightIntensity = texture2D(sTexture, rightTextureCoordinate).r;
    float bottomIntensity = texture2D(sTexture, bottomTextureCoordinate).r;
    float topIntensity = texture2D(sTexture, topTextureCoordinate).r;
    
    vec2 gradientDirection;
    gradientDirection.x = -bottomLeftIntensity - 2.0 * leftIntensity - topLeftIntensity + bottomRightIntensity + 2.0 * rightIntensity + topRightIntensity;
    gradientDirection.y = -topLeftIntensity - 2.0 * topIntensity - topRightIntensity + bottomLeftIntensity + 2.0 * bottomIntensity + bottomRightIntensity;
    
    float gradientMagnitude = length(gradientDirection);
    vec2 normalizedDirection = normalize(gradientDirection);
    normalizedDirection = sign(normalizedDirection) * floor(abs(normalizedDirection) + 0.617316); // Offset by 1-sin(pi/8) to set to 0 if near axis, 1 if away
    normalizedDirection = (normalizedDirection + 1.0) * 0.5; // Place -1.0 - 1.0 within 0 - 1.0
    
    gl_FragColor = vec4(gradientMagnitude, normalizedDirection.x, normalizedDirection.y, 1.0);
}