varying highp vec2 vTextureCoord;
varying highp vec2 vTextureCoord2;

uniform sampler2D sTexture;
uniform sampler2D sTexture2;

void main() {
    mediump vec4 base = texture2D(sTexture, vTextureCoord);
    mediump vec4 overlay = texture2D(sTexture2, vTextureCoord2);
    
    mediump float ra;
    if (2.0 * base.r < base.a) {
        ra = 2.0 * overlay.r * base.r + overlay.r * (1.0 - base.a) + base.r * (1.0 - overlay.a);
    } else {
        ra = overlay.a * base.a - 2.0 * (base.a - base.r) * (overlay.a - overlay.r) + overlay.r * (1.0 - base.a) + base.r * (1.0 - overlay.a);
    }
    
    mediump float ga;
    if (2.0 * base.g < base.a) {
        ga = 2.0 * overlay.g * base.g + overlay.g * (1.0 - base.a) + base.g * (1.0 - overlay.a);
    } else {
        ga = overlay.a * base.a - 2.0 * (base.a - base.g) * (overlay.a - overlay.g) + overlay.g * (1.0 - base.a) + base.g * (1.0 - overlay.a);
    }
    
    mediump float ba;
    if (2.0 * base.b < base.a) {
        ba = 2.0 * overlay.b * base.b + overlay.b * (1.0 - base.a) + base.b * (1.0 - overlay.a);
    } else {
        ba = overlay.a * base.a - 2.0 * (base.a - base.b) * (overlay.a - overlay.b) + overlay.b * (1.0 - base.a) + base.b * (1.0 - overlay.a);
    }
    
    gl_FragColor = vec4(ra, ga, ba, 1.0);
}