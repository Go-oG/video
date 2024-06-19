precision mediump float;
varying vec2 vTextureCoord;
uniform lowp sampler2D sTexture;

const highp vec3 weight = vec3(0.2125, 0.7154, 0.0721);


void main() {
    vec4 color = texture2D(sTexture, vTextureCoord);
    float r = dot(color.rgb, vec3(0.393, 0.769, 0.189));
    float g = dot(color.rgb, vec3(.349, .686, .168));
    float b = dot(color.rgb, vec3(.272, .534, .131));
    gl_FragColor = vec4(r, g, b, color.a);
}