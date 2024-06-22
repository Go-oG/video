precision mediump float;

varying vec2 vTextureCoord;

varying vec2 leftCoord;
varying vec2 topCoord;
varying vec2 rightCoord;
varying vec2 bottomCoord;

varying vec2 leftTopCoord;
varying vec2 rightTopCoord;
varying vec2 leftBottomCoord;
varying vec2 rightBottomCoord;

uniform sampler2D sTexture;

void main() {
    float bottomLeftIntensity = texture2D(sTexture, leftBottomCoord).r;
    float topRightIntensity = texture2D(sTexture, rightTopCoord).r;
    float topLeftIntensity = texture2D(sTexture, leftTopCoord).r;
    float bottomRightIntensity = texture2D(sTexture, rightBottomCoord).r;
    float leftIntensity = texture2D(sTexture, leftCoord).r;
    float rightIntensity = texture2D(sTexture, rightCoord).r;
    float bottomIntensity = texture2D(sTexture, bottomCoord).r;
    float topIntensity = texture2D(sTexture, topCoord).r;

    vec2 gradientDir;
    gradientDir.x = -bottomLeftIntensity - 2.0 * leftIntensity - topLeftIntensity + bottomRightIntensity + 2.0 * rightIntensity + topRightIntensity;
    gradientDir.y = -topLeftIntensity - 2.0 * topIntensity - topRightIntensity + bottomLeftIntensity + 2.0 * bottomIntensity + bottomRightIntensity;

    float gradientMagnitude = length(gradientDir);
    vec2 normalizedDir = normalize(gradientDir);
    normalizedDir = sign(normalizedDir) * floor(abs(normalizedDir) + 0.617316);
    normalizedDir = (normalizedDir + 1.0) * 0.5;

    gl_FragColor = vec4(gradientMagnitude, normalizedDir.x, normalizedDir.y, 1.0);
}