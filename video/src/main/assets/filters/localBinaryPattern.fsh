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


void main() {
    lowp float centerIntensity = texture2D(sTexture, vTextureCoord).r;
    lowp float bottomLeftIntensity = texture2D(sTexture, bottomLeftTextureCoordinate).r;
    lowp float topRightIntensity = texture2D(sTexture, topRightTextureCoordinate).r;
    lowp float topLeftIntensity = texture2D(sTexture, topLeftTextureCoordinate).r;
    lowp float bottomRightIntensity = texture2D(sTexture, bottomRightTextureCoordinate).r;
    lowp float leftIntensity = texture2D(sTexture, leftTextureCoordinate).r;
    lowp float rightIntensity = texture2D(sTexture, rightTextureCoordinate).r;
    lowp float bottomIntensity = texture2D(sTexture, bottomTextureCoordinate).r;
    lowp float topIntensity = texture2D(sTexture, topTextureCoordinate).r;

    lowp float byteTally = 1.0 / 255.0 * step(centerIntensity, topRightIntensity);
    byteTally += 2.0 / 255.0 * step(centerIntensity, topIntensity);
    byteTally += 4.0 / 255.0 * step(centerIntensity, topLeftIntensity);
    byteTally += 8.0 / 255.0 * step(centerIntensity, leftIntensity);
    byteTally += 16.0 / 255.0 * step(centerIntensity, bottomLeftIntensity);
    byteTally += 32.0 / 255.0 * step(centerIntensity, bottomIntensity);
    byteTally += 64.0 / 255.0 * step(centerIntensity, bottomRightIntensity);
    byteTally += 128.0 / 255.0 * step(centerIntensity, rightIntensity);
         
    // TODO: Replace the above with a dot product and two vec4s
    // TODO: Apply step to a matrix, rather than individually
    
    gl_FragColor = vec4(byteTally, byteTally, byteTally, 1.0);
}