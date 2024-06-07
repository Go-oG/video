precision mediump float;

varying vec2 vTextureCoord;
uniform sampler2D sTexture;
uniform float uScale;
uniform vec2 uResolution;

float rand(vec2 uv) {
    return fract(sin(dot(uv, vec2(1225.6548, 321.8942))) * 4251.4865);
}

void main() {
    vec2 ps = vec2(1.0) / uResolution.xy;
    vec2 uv = vTextureCoord.xy * ps;
    vec2 offset = (rand(uv) - 0.5) * 2.0 * ps * uScale;
    vec3 noise = texture2D(sTexture, uv + offset).rgb;
    gl_FragColor = vec4(noise, 1.0);
}
