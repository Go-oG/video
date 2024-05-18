precision highp float;

varying vec2 vTextureCoord;
uniform sampler2D sTexture;
uniform sampler2D sTexture2;

varying vec2 pointATexCoord;
varying vec2 pointBTexCoord;
varying vec2 pointCTexCoord;
varying vec2 pointDTexCoord;
varying vec2 pointETexCoord;
varying vec2 pointFTexCoord;
varying vec2 pointGTexCoord;
varying vec2 pointHTexCoord;

const float PITwo = 6.2832;
const float PI = 3.1416;

void main() {
    vec3 centerColor = texture2D(sTexture, vTextureCoord).rgb;
    vec3 pointAColor = texture2D(sTexture2, pointATexCoord).rgb;
    vec3 pointBColor = texture2D(sTexture2, pointBTexCoord).rgb;
    vec3 pointCColor = texture2D(sTexture2, pointCTexCoord).rgb;
    vec3 pointDColor = texture2D(sTexture2, pointDTexCoord).rgb;
    vec3 pointEColor = texture2D(sTexture2, pointETexCoord).rgb;
    vec3 pointFColor = texture2D(sTexture2, pointFTexCoord).rgb;
    vec3 pointGColor = texture2D(sTexture2, pointGTexCoord).rgb;
    vec3 pointHColor = texture2D(sTexture2, pointHTexCoord).rgb;

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