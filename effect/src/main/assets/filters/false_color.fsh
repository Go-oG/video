precision mediump float;

const vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);
varying highp vec2 vTextureCoord;

uniform sampler2D sTexture;

uniform float uIntensity;
uniform vec3 uFirstColor;
uniform vec3 uSecondColor;


void main() {
    vec4 textureColor = texture2D(sTexture, vTextureCoord);
    float luminance = dot(textureColor.rgb, luminanceWeighting);
    gl_FragColor = vec4(mix(uFirstColor.rgb, uSecondColor.rgb, luminance), textureColor.a);
}