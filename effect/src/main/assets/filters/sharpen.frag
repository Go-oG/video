precision mediump float;
uniform lowp sampler2D sTexture;
varying vec2 textureCoordinate;
varying vec2 leftTextureCoordinate;
varying vec2 rightTextureCoordinate;
varying vec2 topTextureCoordinate;
varying vec2 bottomTextureCoordinate;
varying float centerMultiplier;
varying float edgeMultiplier;

void main() {
    vec3 textureColor = texture2D(sTexture, textureCoordinate).rgb;
    vec3 leftTextureColor = texture2D(sTexture, leftTextureCoordinate).rgb;
    vec3 rightTextureColor = texture2D(sTexture, rightTextureCoordinate).rgb;
    vec3 topTextureColor = texture2D(sTexture, topTextureCoordinate).rgb;
    vec3 bottomTextureColor = texture2D(sTexture, bottomTextureCoordinate).rgb;
    gl_FragColor = vec4((textureColor * centerMultiplier - (leftTextureColor * edgeMultiplier + rightTextureColor * edgeMultiplier + topTextureColor * edgeMultiplier + bottomTextureColor * edgeMultiplier)), texture2D(sTexture, bottomTextureCoordinate).w);
}