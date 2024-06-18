precision mediump float;
varying vec2 vTextureCoord;

uniform lowp sampler2D sTexture;
uniform float red;
uniform float green;
uniform float blue;
uniform float brightness;
uniform float saturation;
uniform float contrast;

const mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);

void main() {
    vec4 textureColor = texture2D(sTexture, vTextureCoord);
    vec4 textureOtherColor = texture2D(sTexture, vTextureCoord);
    float luminance = dot(textureOtherColor.rgb, luminanceWeighting);
    vec3 greyScaleColor = vec3(luminance);

    gl_FragColor = vec4(textureColor.r * red, textureColor.g * green, textureColor.b * blue, 1.0);
    gl_FragColor = vec4((textureOtherColor.rgb + vec3(brightness)), textureOtherColor.w);
    gl_FragColor = vec4(mix(greyScaleColor, textureColor.rgb, saturation), textureOtherColor.w);
    gl_FragColor = vec4(((textureOtherColor.rgb - vec3(0.5)) * contrast + vec3(0.5)), textureOtherColor.w);
}