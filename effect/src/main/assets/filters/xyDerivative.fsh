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
    float topIntensity = texture2D(sTexture, topTextureCoord).r;
    float topRightIntensity = texture2D(sTexture, topRightTextureCoord).r;
    float topLeftIntensity = texture2D(sTexture, topLeftTextureCoord).r;
    float bottomIntensity = texture2D(sTexture, bottomTextureCoord).r;
    float bottomLeftIntensity = texture2D(sTexture, bottomLeftTextureCoord).r;
    float bottomRightIntensity = texture2D(sTexture, bottomRightTextureCoord).r;
    float leftIntensity = texture2D(sTexture, leftTextureCoord).r;
    float rightIntensity = texture2D(sTexture, rightTextureCoord).r;
    
    float verticalDerivative = -topLeftIntensity - topIntensity - topRightIntensity + bottomLeftIntensity + bottomIntensity + bottomRightIntensity;
    float horizontalDerivative = -bottomLeftIntensity - leftIntensity - topLeftIntensity + bottomRightIntensity + rightIntensity + topRightIntensity;
    verticalDerivative = verticalDerivative;
    horizontalDerivative = horizontalDerivative;
    
    // Scaling the X * Y operation so that negative numbers are not clipped in the 0..1 range. This will be expanded in the corner detection filter
    gl_FragColor = vec4(horizontalDerivative * horizontalDerivative, verticalDerivative * verticalDerivative, ((verticalDerivative * horizontalDerivative) + 1.0) / 2.0, 1.0);
}
