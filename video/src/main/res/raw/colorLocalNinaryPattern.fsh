#version 100

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
    lowp vec3 centerColor = texture2D(sTexture, vTextureCoord).rgb;
    lowp vec3 bottomLeftColor = texture2D(sTexture, bottomLeftTextureCoordinate).rgb;
    lowp vec3 topRightColor = texture2D(sTexture, topRightTextureCoordinate).rgb;
    lowp vec3 topLeftColor = texture2D(sTexture, topLeftTextureCoordinate).rgb;
    lowp vec3 bottomRightColor = texture2D(sTexture, bottomRightTextureCoordinate).rgb;
    lowp vec3 leftColor = texture2D(sTexture, leftTextureCoordinate).rgb;
    lowp vec3 rightColor = texture2D(sTexture, rightTextureCoordinate).rgb;
    lowp vec3 bottomColor = texture2D(sTexture, bottomTextureCoordinate).rgb;
    lowp vec3 topColor = texture2D(sTexture, topTextureCoordinate).rgb;

    lowp float redByteTally = 1.0 / 255.0 * step(centerColor.r, topRightColor.r);
    redByteTally += 2.0 / 255.0 * step(centerColor.r, topColor.r);
    redByteTally += 4.0 / 255.0 * step(centerColor.r, topLeftColor.r);
    redByteTally += 8.0 / 255.0 * step(centerColor.r, leftColor.r);
    redByteTally += 16.0 / 255.0 * step(centerColor.r, bottomLeftColor.r);
    redByteTally += 32.0 / 255.0 * step(centerColor.r, bottomColor.r);
    redByteTally += 64.0 / 255.0 * step(centerColor.r, bottomRightColor.r);
    redByteTally += 128.0 / 255.0 * step(centerColor.r, rightColor.r);

    lowp float blueByteTally = 1.0 / 255.0 * step(centerColor.b, topRightColor.b);
    blueByteTally += 2.0 / 255.0 * step(centerColor.b, topColor.b);
    blueByteTally += 4.0 / 255.0 * step(centerColor.b, topLeftColor.b);
    blueByteTally += 8.0 / 255.0 * step(centerColor.b, leftColor.b);
    blueByteTally += 16.0 / 255.0 * step(centerColor.b, bottomLeftColor.b);
    blueByteTally += 32.0 / 255.0 * step(centerColor.b, bottomColor.b);
    blueByteTally += 64.0 / 255.0 * step(centerColor.b, bottomRightColor.b);
    blueByteTally += 128.0 / 255.0 * step(centerColor.b, rightColor.b);

    lowp float greenByteTally = 1.0 / 255.0 * step(centerColor.g, topRightColor.g);
    greenByteTally += 2.0 / 255.0 * step(centerColor.g, topColor.g);
    greenByteTally += 4.0 / 255.0 * step(centerColor.g, topLeftColor.g);
    greenByteTally += 8.0 / 255.0 * step(centerColor.g, leftColor.g);
    greenByteTally += 16.0 / 255.0 * step(centerColor.g, bottomLeftColor.g);
    greenByteTally += 32.0 / 255.0 * step(centerColor.g, bottomColor.g);
    greenByteTally += 64.0 / 255.0 * step(centerColor.g, bottomRightColor.g);
    greenByteTally += 128.0 / 255.0 * step(centerColor.g, rightColor.g);

    // TODO: Replace the above with a dot product and two vec4s
    // TODO: Apply step to a matrix, rather than individually

    gl_FragColor = vec4(redByteTally, blueByteTally, greenByteTally, 1.0);
}