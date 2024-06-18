precision mediump float;

varying highp vec2 vTextureCoord;
uniform sampler2D sTexture;

uniform float shadowTintIntensity;
uniform float highlightTintIntensity;
uniform vec3 shadowTintColor;
uniform vec3 highlightTintColor;

const vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);

void main() {
   vec4 textureColor = texture2D(sTexture, vTextureCoord);
   float luminance = dot(textureColor.rgb, luminanceWeighting);

   vec4 shadowResult = mix(textureColor, max(textureColor, vec4(mix(shadowTintColor, textureColor.rgb, luminance), textureColor.a)), shadowTintIntensity);
   vec4 highlightResult = mix(textureColor, min(shadowResult, vec4(mix(shadowResult.rgb, highlightTintColor, luminance), textureColor.a)), highlightTintIntensity);

   gl_FragColor = vec4( mix(shadowResult.rgb, highlightResult.rgb, luminance), textureColor.a);
}