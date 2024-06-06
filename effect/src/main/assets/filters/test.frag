vec2 blurOpticalFlow(vec2 uv, sampler2D flowTex) {
    float blurRadius = BLUR;
    vec2 texelSize = 1.0 / iResolution.xy;

    vec2 flow = vec2(0.0);
    float totalWeight = 0.0;

    for (float x = -blurRadius; x <= blurRadius; x += 2.0) {
        for (float y = -blurRadius; y <= blurRadius; y += 2.0) {
            vec2 offset = vec2(x, y) * texelSize;
            vec2 sampleFlow = texture(flowTex, uv + offset).rg;
            flow += sampleFlow;
            totalWeight += 1.0;
        }
    }

    return flow / totalWeight;
}

void mainImage(out vec4 fragColor, in vec2 fragCoord) {
    vec2 uv = fragCoord.xy / iResolution.xy;

    const int numSamples = SAMPLES;

    vec2 flow = blurOpticalFlow(uv, iChannel1);
    flow *= iResolution.xy;

    vec4 color = vec4(0.0);
    float totalWeight = 0.0;
    for (int i = 0; i < numSamples; i++) {

        float t = float(i) / float(numSamples) * INTENSITY;
        vec2 sampleUV = uv + flow * (t * SPREAD - 0.5) / iResolution.xy;
        vec4 sampleColor = texture(iChannel0, sampleUV);
        color += sampleColor;
        totalWeight += 1.0;
    }

    color /= totalWeight;
    fragColor = color;
}





