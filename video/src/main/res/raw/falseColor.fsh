precision lowp float;

varying highp vec2 vTextureCoord;
uniform sampler2D sTexture;

uniform float intensity;
uniform vec3 firstColor;
uniform vec3 secondColor;

const mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);

void main()
{
    lowp vec4 textureColor = texture2D(sTexture, vTextureCoord);
    float luminance = dot(textureColor.rgb, luminanceWeighting);
    
    gl_FragColor = vec4( mix(firstColor.rgb, secondColor.rgb, luminance), textureColor.a);
}