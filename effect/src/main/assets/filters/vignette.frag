precision mediump float;
varying vec2 vTextureCoord;
uniform lowp sampler2D sTexture;
uniform lowp vec2 vignetteCenter;
uniform highp float vignetteStart;
uniform highp float vignetteEnd;
void main() {
    lowp vec3 rgb = texture2D(sTexture, vTextureCoord).rgb;
    lowp float d = distance(vTextureCoord, vec2(vignetteCenter.x, vignetteCenter.y));
    lowp float percent = smoothstep(vignetteStart, vignetteEnd, d);
    gl_FragColor = vec4(mix(rgb.x, 0.0, percent), mix(rgb.y, 0.0, percent), mix(rgb.z, 0.0, percent), 1.0);
}