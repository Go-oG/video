precision highp float;

uniform sampler2D sTexture;
varying vec2 vTextureCoord;

varying vec2 leftTextureCoord;
varying vec2 rightTextureCoord;

varying vec2 topTextureCoord;
varying vec2 topLeftTextureCoord;
varying vec2 topRightTextureCoord;

varying vec2 bottomTextureCoord;
varying vec2 bottomLeftTextureCoord;
varying vec2 bottomRightTextureCoord;


void main() {
    lowp float centerIntensity = texture2D(sTexture, vTextureCoord).r;
    lowp float bottomLeftIntensity = texture2D(sTexture, bottomLeftTextureCoord).r;
    lowp float topRightIntensity = texture2D(sTexture, topRightTextureCoord).r;
    lowp float topLeftIntensity = texture2D(sTexture, topLeftTextureCoord).r;
    lowp float bottomRightIntensity = texture2D(sTexture, bottomRightTextureCoord).r;
    lowp float leftIntensity = texture2D(sTexture, leftTextureCoord).r;
    lowp float rightIntensity = texture2D(sTexture, rightTextureCoord).r;
    lowp float bottomIntensity = texture2D(sTexture, bottomTextureCoord).r;
    lowp float topIntensity = texture2D(sTexture, topTextureCoord).r;

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