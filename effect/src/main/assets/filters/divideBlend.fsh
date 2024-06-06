varying highp vec2 vTextureCoord;
varying highp vec2 vTextureCoord2;

uniform sampler2D sTexture;
uniform sampler2D sTexture2;

void main() {
    vec4 base = texture2D(sTexture, vTextureCoord);
    vec4 overlay = texture2D(sTexture2, vTextureCoord2);

    float ra;
    if (overlay.a == 0.0 || ((base.r / overlay.r) > (base.a / overlay.a))) {
        ra = overlay.a * base.a + overlay.r * (1.0 - base.a) + base.r * (1.0 - overlay.a);
    } else {
        ra = (base.r * overlay.a * overlay.a) / overlay.r + overlay.r * (1.0 - base.a) + base.r * (1.0 - overlay.a);
    }

    float ga;
    if (overlay.a == 0.0 || ((base.g / overlay.g) > (base.a / overlay.a))) {
        ga = overlay.a * base.a + overlay.g * (1.0 - base.a) + base.g * (1.0 - overlay.a);
    } else {
        ga = (base.g * overlay.a * overlay.a) / overlay.g + overlay.g * (1.0 - base.a) + base.g * (1.0 - overlay.a);
    }

    float ba;
    if (overlay.a == 0.0 || ((base.b / overlay.b) > (base.a / overlay.a))) {
        ba = overlay.a * base.a + overlay.b * (1.0 - base.a) + base.b * (1.0 - overlay.a);
    } else {
        ba = (base.b * overlay.a * overlay.a) / overlay.b + overlay.b * (1.0 - base.a) + base.b * (1.0 - overlay.a);
    }

    float a = overlay.a + base.a - overlay.a * base.a;
  
    gl_FragColor = vec4(ra, ga, ba, a);
}