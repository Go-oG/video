varying highp vec2 vTextureCoord;
varying highp vec2 vTextureCoord2;

uniform sampler2D sTexture;
uniform sampler2D sTexture2;

uniform highp float intensity;

void main() {
    lowp vec3 currentImageColor = texture2D(sTexture, vTextureCoord).rgb;
    lowp vec3 lowPassImageColor = texture2D(sTexture2, vTextureCoord2).rgb;
    
    mediump float colorDistance = distance(currentImageColor, lowPassImageColor); // * 0.57735
    lowp float movementThreshold = step(0.2, colorDistance);
    
    gl_FragColor = movementThreshold * vec4(textureCoordinate2.x, textureCoordinate2.y, 1.0, 1.0);
}