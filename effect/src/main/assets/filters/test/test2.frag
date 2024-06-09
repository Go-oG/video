precision mediump float;
varying vec2 vTextureCoord;
uniform sampler2D sTexture;
uniform vec2 uResolution;

float dist = 4.0;
int loops = 6;

float rand(vec2 uv) {
    return fract(sin(dot(uv, vec2(12.9898, 78.233))) * 43578.5453);
}

void main() {
    vec4 t = vec4(0.0);
    vec2 texel = 1.0 / uResolution.xy;
    vec2 d = texel * dist;
    vec2 uv=vTextureCoord*vec2(1.0,-1.0);
    for (int i = 0; i < loops; i++) {
        float r1 = clamp(rand(uv * float(i)) * 2.0 - 1.0, -d.x, d.x);
        float r2 = clamp(rand(uv * float(i + loops)) * 2.0 - 1.0, -d.y, d.y);
        t += texture2D(sTexture, uv + vec2(r1, r2));
    }
    t /= float(loops);
    gl_FragColor = t;
}