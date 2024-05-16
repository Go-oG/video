precision highp float;

varying vec2 vTextureCoord;
varying vec2 pointATextureCoordinate;
varying vec2 pointBTextureCoordinate;
varying vec2 pointCTextureCoordinate;
varying vec2 pointDTextureCoordinate;
varying vec2 pointETextureCoordinate;
varying vec2 pointFTextureCoordinate;
varying vec2 pointGTextureCoordinate;
varying vec2 pointHTextureCoordinate;

uniform sampler2D sTexture;
uniform sampler2D sTexture2;
const float PITwo = 6.2832;
const float PI = 3.1416;
void main()
{
    vec3 centerColor = texture2D(sTexture, vTextureCoord).rgb;
    
    vec3 pointAColor = texture2D(sTexture2, pointATextureCoordinate).rgb;
    vec3 pointBColor = texture2D(sTexture2, pointBTextureCoordinate).rgb;
    vec3 pointCColor = texture2D(sTexture2, pointCTextureCoordinate).rgb;
    vec3 pointDColor = texture2D(sTexture2, pointDTextureCoordinate).rgb;
    vec3 pointEColor = texture2D(sTexture2, pointETextureCoordinate).rgb;
    vec3 pointFColor = texture2D(sTexture2, pointFTextureCoordinate).rgb;
    vec3 pointGColor = texture2D(sTexture2, pointGTextureCoordinate).rgb;
    vec3 pointHColor = texture2D(sTexture2, pointHTextureCoordinate).rgb;

    vec3 colorComparison = ((pointAColor + pointBColor + pointCColor + pointDColor + pointEColor + pointFColor + pointGColor + pointHColor) * 0.125) - centerColor;

    // Direction calculation drawn from Appendix B of Seth Hall's Ph.D. thesis
    
    vec3 dirX = (pointAColor*0.94868) + (pointBColor*0.316227) - (pointCColor*0.316227) - (pointDColor*0.94868) - (pointEColor*0.94868) - (pointFColor*0.316227) + (pointGColor*0.316227) + (pointHColor*0.94868);
    vec3 dirY = (pointAColor*0.316227) + (pointBColor*0.94868) + (pointCColor*0.94868) + (pointDColor*0.316227) - (pointEColor*0.316227) - (pointFColor*0.94868) - (pointGColor*0.94868) - (pointHColor*0.316227);
    vec3 absoluteDifference = abs(colorComparison);
    float componentLength = length(colorComparison);
    float avgX = dot(absoluteDifference, dirX) / componentLength;
    float avgY = dot(absoluteDifference, dirY) / componentLength;
    float angle = atan(avgY, avgX);
    
    vec3 normalizedColorComparison = (colorComparison + 1.0) * 0.5;
    
    gl_FragColor = vec4(normalizedColorComparison, (angle+PI)/PITwo);
}