precision mediump float;
varying highp vec2 vTextureCoord;
uniform sampler2D sTexture;

uniform float uTemperature;
uniform float uTint;
const lowp vec3 warmFilter = vec3(0.93, 0.54, 0.0);
const mat3 RGBtoYIQ = mat3(0.299, 0.587, 0.114, 0.596, -0.274, -0.322, 0.212, -0.523, 0.311);
const mat3 YIQtoRGB = mat3(1.0, 0.956, 0.621, 1.0, -0.272, -0.647, 1.0, -1.105, 1.702);

void main() {
    vec4 source = texture2D(sTexture, vTextureCoord);
    vec3 yiq = RGBtoYIQ * source.rgb;
    yiq.b = clamp(yiq.b + uTint * 0.5226 * 0.1, -0.5226, 0.5226);
    vec3 rgb = YIQtoRGB * yiq;
    vec3 processed = vec3(
        (rgb.r < 0.5 ? (2.0 * rgb.r * warmFilter.r) : (1.0 - 2.0 * (1.0 - rgb.r) * (1.0 - warmFilter.r))),
        (rgb.g < 0.5 ? (2.0 * rgb.g * warmFilter.g) : (1.0 - 2.0 * (1.0 - rgb.g) * (1.0 - warmFilter.g))),
        (rgb.b < 0.5 ? (2.0 * rgb.b * warmFilter.b) : (1.0 - 2.0 * (1.0 - rgb.b) * (1.0 - warmFilter.b))));
    gl_FragColor = vec4(mix(rgb, processed, uTemperature), source.a);
}