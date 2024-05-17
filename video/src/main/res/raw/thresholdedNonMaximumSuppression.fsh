 uniform sampler2D sTexture;
 
 varying highp vec2 vTextureCoord;
 varying highp vec2 leftTextureCoordinate;
 varying highp vec2 rightTextureCoordinate;
 
 varying highp vec2 topTextureCoordinate;
 varying highp vec2 topLeftTextureCoordinate;
 varying highp vec2 topRightTextureCoordinate;
 
 varying highp vec2 bottomTextureCoordinate;
 varying highp vec2 bottomLeftTextureCoordinate;
 varying highp vec2 bottomRightTextureCoordinate;
 
 uniform lowp float threshold;
 
 void main() {
     lowp float bottomColor = texture2D(sTexture, bottomTextureCoordinate).r;
     lowp float bottomLeftColor = texture2D(sTexture, bottomLeftTextureCoordinate).r;
     lowp float bottomRightColor = texture2D(sTexture, bottomRightTextureCoordinate).r;
     lowp vec4 centerColor = texture2D(sTexture, vTextureCoord);
     lowp float leftColor = texture2D(sTexture, leftTextureCoordinate).r;
     lowp float rightColor = texture2D(sTexture, rightTextureCoordinate).r;
     lowp float topColor = texture2D(sTexture, topTextureCoordinate).r;
     lowp float topRightColor = texture2D(sTexture, topRightTextureCoordinate).r;
     lowp float topLeftColor = texture2D(sTexture, topLeftTextureCoordinate).r;
     
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