precision mediump float;

varying highp vec2 vTextureCoord;
varying highp vec2 vTextureCoord2;

uniform sampler2D sTexture;
uniform sampler2D sTexture2;

void main()
{
    vec4 base = texture2D(sTexture, vTextureCoord);
    vec4 overlay = texture2D(sTexture2, vTextureCoord2);

    vec3 baseOverlayAlphaProduct = vec3(overlay.a * base.a);
    vec3 rightHandProduct = overlay.rgb * (1.0 - base.a) + base.rgb * (1.0 - overlay.a);

    vec3 firstBlendColor = baseOverlayAlphaProduct + rightHandProduct;
    vec3 overlayRGB = clamp((overlay.rgb / clamp(overlay.a, 0.01, 1.0)) * step(0.0, overlay.a), 0.0, 0.99);

    vec3 secondBlendColor = (base.rgb * overlay.a) / (1.0 - overlayRGB) + rightHandProduct;

    vec3 colorChoice = step((overlay.rgb * base.a + base.rgb * overlay.a), baseOverlayAlphaProduct);

    gl_FragColor = vec4(mix(firstBlendColor, secondBlendColor, colorChoice), 1.0);
}