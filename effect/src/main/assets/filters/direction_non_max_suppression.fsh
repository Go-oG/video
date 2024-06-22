precision mediump float;

varying vec2 vTextureCoord;
uniform sampler2D sTexture;

uniform float uPixelWidth;
uniform float uPixelHeight;
uniform float uUpperThreshold;
uniform float uLowerThreshold;

void main() {
    vec3 curGradientAndDirect = texture2D(sTexture, vTextureCoord).rgb;
    vec2 gradientDirect = ((curGradientAndDirect.gb * 2.0) - 1.0) * vec2(uPixelWidth, uPixelHeight);

    float firstGradientMagnitude = texture2D(sTexture, vTextureCoord + gradientDirect).r;
    float secondGradientMagnitude = texture2D(sTexture, vTextureCoord - gradientDirect).r;

    float multiplier = step(firstGradientMagnitude, curGradientAndDirect.r);
    multiplier = multiplier * step(secondGradientMagnitude, curGradientAndDirect.r);

    float thresholdCompliance = smoothstep(uLowerThreshold, uUpperThreshold, curGradientAndDirect.r);
    multiplier = multiplier * thresholdCompliance;

    gl_FragColor = vec4(multiplier, multiplier, multiplier, 1.0);
}