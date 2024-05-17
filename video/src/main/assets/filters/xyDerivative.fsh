// I'm using the Prewitt operator to obtain the derivative, then squaring the X and Y components and placing the product of the two in Z.
// In tests, Prewitt seemed to be tied with Sobel for the best, and it's just a little cheaper to compute.
// This is primarily intended to be used with corner detection filters.

precision highp float;

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
    float topIntensity = texture2D(sTexture, topTextureCoordinate).r;
    float topRightIntensity = texture2D(sTexture, topRightTextureCoordinate).r;
    float topLeftIntensity = texture2D(sTexture, topLeftTextureCoordinate).r;
    float bottomIntensity = texture2D(sTexture, bottomTextureCoordinate).r;
    float bottomLeftIntensity = texture2D(sTexture, bottomLeftTextureCoordinate).r;
    float bottomRightIntensity = texture2D(sTexture, bottomRightTextureCoordinate).r;
    float leftIntensity = texture2D(sTexture, leftTextureCoordinate).r;
    float rightIntensity = texture2D(sTexture, rightTextureCoordinate).r;
    
    float verticalDerivative = -topLeftIntensity - topIntensity - topRightIntensity + bottomLeftIntensity + bottomIntensity + bottomRightIntensity;
    float horizontalDerivative = -bottomLeftIntensity - leftIntensity - topLeftIntensity + bottomRightIntensity + rightIntensity + topRightIntensity;
    verticalDerivative = verticalDerivative;
    horizontalDerivative = horizontalDerivative;
    
    // Scaling the X * Y operation so that negative numbers are not clipped in the 0..1 range. This will be expanded in the corner detection filter
    gl_FragColor = vec4(horizontalDerivative * horizontalDerivative, verticalDerivative * verticalDerivative, ((verticalDerivative * horizontalDerivative) + 1.0) / 2.0, 1.0);
}