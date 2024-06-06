precision mediump float;

varying vec2 vTextureCoord;
uniform sampler2D sTexture;

void main() {
    vec3 color = vec3(0.0);
    int iterationCount = 3;
    float direction = 0.25;

    for (int k = -iterationCount; k < iterationCount; k++) {
        vec2 sampleCoord = vTextureCoord - direction * float(k);
        sampleCoord = clamp(sampleCoord, vec2(0.0), vec2(1.0));
        color += texture2D(sTexture, sampleCoord).rgb;
    }
    gl_FragColor = vec4(color / (iterationCount * 2.0), 1.0);

}