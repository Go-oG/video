varying highp vec2 vTextureCoord;
varying highp vec2 vTextureCoord2;

uniform sampler2D sTexture;
uniform sampler2D sTexture2;

uniform highp float topFocusLevel;
uniform highp float bottomFocusLevel;
uniform highp float focusFallOffRate;

void main() {
    lowp vec4 sharpImageColor = texture2D(sTexture, vTextureCoord);
    lowp vec4 blurredImageColor = texture2D(sTexture2, vTextureCoord2);
    
    lowp float blurIntensity = 1.0 - smoothstep(topFocusLevel - focusFallOffRate, topFocusLevel, vTextureCoord2.y);
    blurIntensity += smoothstep(bottomFocusLevel, bottomFocusLevel + focusFallOffRate, vTextureCoord2.y);
    
    gl_FragColor = mix(sharpImageColor, blurredImageColor, blurIntensity);
}