//   Code from "Graphics Shaders: Theory and Practice" by M. Bailey and S. Cunningham 

precision mediump float;

varying vec2 vTextureCoord;
varying vec2 leftTextureCoord;
varying vec2 rightTextureCoord;

varying vec2 topTextureCoord;
varying vec2 topLeftTextureCoord;
varying vec2 topRightTextureCoord;

varying vec2 bottomTextureCoord;
varying vec2 bottomLeftTextureCoord;
varying vec2 bottomRightTextureCoord;

uniform sampler2D sTexture;
uniform float edgeStrength;

void main() {
   float bottomLeftIntensity = texture2D(sTexture, bottomLeftTextureCoord).r;
   float topRightIntensity = texture2D(sTexture, topRightTextureCoord).r;
   float topLeftIntensity = texture2D(sTexture, topLeftTextureCoord).r;
   float bottomRightIntensity = texture2D(sTexture, bottomRightTextureCoord).r;
   float leftIntensity = texture2D(sTexture, leftTextureCoord).r;
   float rightIntensity = texture2D(sTexture, rightTextureCoord).r;
   float bottomIntensity = texture2D(sTexture, bottomTextureCoord).r;
   float topIntensity = texture2D(sTexture, topTextureCoord).r;
   float h = -topLeftIntensity - 2.0 * topIntensity - topRightIntensity + bottomLeftIntensity + 2.0 * bottomIntensity + bottomRightIntensity;
   float v = -bottomLeftIntensity - 2.0 * leftIntensity - topLeftIntensity + bottomRightIntensity + 2.0 * rightIntensity + topRightIntensity;
   
   float mag = length(vec2(h, v)) * edgeStrength;
   
   gl_FragColor = vec4(vec3(mag), 1.0);
}