 uniform sampler2D sTexture;
 varying highp vec2 vTextureCoord;

 varying highp vec2 leftTextureCoord;
 varying highp vec2 topTextureCoord;
 varying highp vec2 rightTextureCoord;
 varying highp vec2 bottomTextureCoord;
 varying highp vec2 topLeftTextureCoord;
 varying highp vec2 topRightTextureCoord;
 varying highp vec2 bottomLeftTextureCoord;
 varying highp vec2 bottomRightTextureCoord;
 
 uniform lowp float threshold;
 
 void main() {
     lowp float bottomColor = texture2D(sTexture, bottomTextureCoord).r;
     lowp float bottomLeftColor = texture2D(sTexture, bottomLeftTextureCoord).r;
     lowp float bottomRightColor = texture2D(sTexture, bottomRightTextureCoord).r;
     lowp vec4 centerColor = texture2D(sTexture, vTextureCoord);
     lowp float leftColor = texture2D(sTexture, leftTextureCoord).r;
     lowp float rightColor = texture2D(sTexture, rightTextureCoord).r;
     lowp float topColor = texture2D(sTexture, topTextureCoord).r;
     lowp float topRightColor = texture2D(sTexture, topRightTextureCoord).r;
     lowp float topLeftColor = texture2D(sTexture, topLeftTextureCoord).r;
     
     // Use a tiebreaker for pixels to the left and immediately above this one
     lowp float multiplier = 1.0 - step(centerColor.r, topColor);
     multiplier = multiplier * (1.0 - step(centerColor.r, topLeftColor));
     multiplier = multiplier * (1.0 - step(centerColor.r, leftColor));
     multiplier = multiplier * (1.0 - step(centerColor.r, bottomLeftColor));
     
     lowp float maxValue = max(centerColor.r, bottomColor);
     maxValue = max(maxValue, bottomRightColor);
     maxValue = max(maxValue, rightColor);
     maxValue = max(maxValue, topRightColor);
     
     lowp float finalValue = centerColor.r * step(maxValue, centerColor.r) * multiplier;
     finalValue = step(threshold, finalValue);
     
     gl_FragColor = vec4(finalValue, finalValue, finalValue, 1.0);
//
//     gl_FragColor = vec4((centerColor.rgb * step(maxValue, step(threshold, centerColor.r)) * multiplier), 1.0);
 }