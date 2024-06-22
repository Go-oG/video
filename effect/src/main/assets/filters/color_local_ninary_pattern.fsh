precision mediump float;

uniform sampler2D sTexture;

varying vec2 vTextureCoord;

varying vec2 leftCoord;
varying vec2 topCoord;
varying vec2 rightCoord;
varying vec2 bottomCoord;

varying vec2 leftTopCoord;
varying vec2 rightTopCoord;
varying vec2 leftBottomCoord;
varying vec2 rightBottomCoord;

void main() {
    vec3 centerColor = texture2D(sTexture, vTextureCoord).rgb;
    vec3 bottomLeftColor = texture2D(sTexture, leftBottomCoord).rgb;
    vec3 topRightColor = texture2D(sTexture, rightTopCoord).rgb;
    vec3 topLeftColor = texture2D(sTexture, leftTopCoord).rgb;
    vec3 bottomRightColor = texture2D(sTexture, rightBottomCoord).rgb;
    vec3 leftColor = texture2D(sTexture, leftCoord).rgb;
    vec3 rightColor = texture2D(sTexture, rightCoord).rgb;
    vec3 bottomColor = texture2D(sTexture, bottomCoord).rgb;
    vec3 topColor = texture2D(sTexture, topCoord).rgb;

    float redByteTally = 1.0 / 255.0 * step(centerColor.r, topRightColor.r);
    redByteTally += 2.0 / 255.0 * step(centerColor.r, topColor.r);
    redByteTally += 4.0 / 255.0 * step(centerColor.r, topLeftColor.r);
    redByteTally += 8.0 / 255.0 * step(centerColor.r, leftColor.r);
    redByteTally += 16.0 / 255.0 * step(centerColor.r, bottomLeftColor.r);
    redByteTally += 32.0 / 255.0 * step(centerColor.r, bottomColor.r);
    redByteTally += 64.0 / 255.0 * step(centerColor.r, bottomRightColor.r);
    redByteTally += 128.0 / 255.0 * step(centerColor.r, rightColor.r);

    float blueByteTally = 1.0 / 255.0 * step(centerColor.b, topRightColor.b);
    blueByteTally += 2.0 / 255.0 * step(centerColor.b, topColor.b);
    blueByteTally += 4.0 / 255.0 * step(centerColor.b, topLeftColor.b);
    blueByteTally += 8.0 / 255.0 * step(centerColor.b, leftColor.b);
    blueByteTally += 16.0 / 255.0 * step(centerColor.b, bottomLeftColor.b);
    blueByteTally += 32.0 / 255.0 * step(centerColor.b, bottomColor.b);
    blueByteTally += 64.0 / 255.0 * step(centerColor.b, bottomRightColor.b);
    blueByteTally += 128.0 / 255.0 * step(centerColor.b, rightColor.b);

    float greenByteTally = 1.0 / 255.0 * step(centerColor.g, topRightColor.g);
    greenByteTally += 2.0 / 255.0 * step(centerColor.g, topColor.g);
    greenByteTally += 4.0 / 255.0 * step(centerColor.g, topLeftColor.g);
    greenByteTally += 8.0 / 255.0 * step(centerColor.g, leftColor.g);
    greenByteTally += 16.0 / 255.0 * step(centerColor.g, bottomLeftColor.g);
    greenByteTally += 32.0 / 255.0 * step(centerColor.g, bottomColor.g);
    greenByteTally += 64.0 / 255.0 * step(centerColor.g, bottomRightColor.g);
    greenByteTally += 128.0 / 255.0 * step(centerColor.g, rightColor.g);


    gl_FragColor = vec4(redByteTally, blueByteTally, greenByteTally, 1.0);
}