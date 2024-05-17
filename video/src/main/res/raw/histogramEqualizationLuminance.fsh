varying highp vec2 vTextureCoord;
uniform sampler2D sTexture;
uniform sampler2D sTexture2;

const lowp vec3 W = vec3(0.2125, 0.7154, 0.0721);

void main() {
    lowp vec4 textureColor = texture2D(sTexture, vTextureCoord);
    lowp float luminance = dot(textureColor.rgb, W);
    lowp float newLuminance = texture2D(sTexture2, vec2(luminance, 0.0)).r;
    lowp float deltaLuminance = newLuminance - luminance;
    
    lowp float red   = clamp(textureColor.r + deltaLuminance, 0.0, 1.0);
    lowp float green = clamp(textureColor.g + deltaLuminance, 0.0, 1.0);
    lowp float blue  = clamp(textureColor.b + deltaLuminance, 0.0, 1.0);

    gl_FragColor = vec4(red, green, blue, textureColor.a);
}