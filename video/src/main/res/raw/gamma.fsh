varying highp vec2 vTextureCoord;

uniform sampler2D sTexture;
uniform lowp float gamma;

void main() {
    lowp vec4 textureColor = texture2D(sTexture, vTextureCoord);
    
    gl_FragColor = vec4(pow(textureColor.rgb, vec3(gamma)), textureColor.w);
}