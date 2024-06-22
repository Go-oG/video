varying highp vec2 vTextureCoord;
const float harrisConstant = 0.04;

uniform sampler2D sTexture;

uniform  float uSensitivity;

void main() {
    vec3 derivativeElements = texture2D(sTexture, vTextureCoord).rgb;

    float derivativeSum = derivativeElements.x + derivativeElements.y;

    float zElement = (derivativeElements.z * 2.0) - 1.0;

    // R = Ix^2 * Iy^2 - Ixy * Ixy - k * (Ix^2 + Iy^2)^2
    float cornerness = derivativeElements.x * derivativeElements.y - (zElement * zElement) - harrisConstant * derivativeSum * derivativeSum;

    gl_FragColor = vec4(vec3(cornerness * uSensitivity), 1.0);
}