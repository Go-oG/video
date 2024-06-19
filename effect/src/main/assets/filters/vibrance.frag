precision mediump float;
varying vec2 vTextureCoord;

uniform lowp sampler2D sTexture;
uniform float uVibrance;

void main() {
    vec4 color = texture2D(sTexture, vTextureCoord);
    float average = (color.r + color.g + color.b) / 3.0;
    float mx = max(color.r, max(color.g, color.b));
    float amt = (mx - average) * (-uVibrance * 3.0);
    color.rgb = mix(color.rgb, vec3(mx), amt);
    gl_FragColor = color;
}