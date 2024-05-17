varying highp vec2 vTextureCoord;

uniform sampler2D sTexture;
uniform lowp float sensitivity;

const mediump float harrisConstant = 0.04;

void main() {
    mediump vec3 derivativeElements = texture2D(sTexture, vTextureCoord).rgb;
    
    mediump float derivativeSum = derivativeElements.x + derivativeElements.y;
    
    mediump float zElement = (derivativeElements.z * 2.0) - 1.0;

    // R = Ix^2 * Iy^2 - Ixy * Ixy - k * (Ix^2 + Iy^2)^2
    mediump float cornerness = derivativeElements.x * derivativeElements.y - (zElement * zElement) - harrisConstant * derivativeSum * derivativeSum;

    gl_FragColor = vec4(vec3(cornerness * sensitivity), 1.0);
}