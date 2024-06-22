precision mediump float;

varying vec2 vTextureCoord;
uniform lowp sampler2D sTexture;
const vec4 colorCyan = vec4(85.0 / 255.0, 1.0, 1.0, 1.0);
const vec4 colorMagenta = vec4(1.0, 85.0 / 255.0, 1.0, 1.0);
const vec4 colorWhite = vec4(1.0, 1.0, 1.0, 1.0);
const vec4 colorBlack = vec4(0.0, 0.0, 0.0, 1.0);

uniform float uPixelWidth;
uniform float uPixelheight;


void main() {
    vec2 sampleDivisor = vec2(uPixelWidth,uPixelheight);

    vec2 samplePos = vTextureCoord - mod(vTextureCoord, sampleDivisor);
    vec4 color = texture2D(sTexture, samplePos);
    vec4 endColor;
    float blackDistance = distance(color, colorBlack);
    float whiteDistance = distance(color, colorWhite);
    float magentaDistance = distance(color, colorMagenta);
    float cyanDistance = distance(color, colorCyan);
    vec4 finalColor;
    float colorDistance = min(magentaDistance, cyanDistance);
    colorDistance = min(colorDistance, whiteDistance);
    colorDistance = min(colorDistance, blackDistance);
    if (colorDistance == blackDistance) {
        finalColor = colorBlack;
    } else if (colorDistance == whiteDistance) {
        finalColor = colorWhite;
    } else if (colorDistance == cyanDistance) {
        finalColor = colorCyan;
    } else {
        finalColor = colorMagenta;
    }
    gl_FragColor = finalColor;
}