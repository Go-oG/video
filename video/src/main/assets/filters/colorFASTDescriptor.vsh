attribute vec4 aPosition;
attribute vec4 aTextureCoord;
attribute vec4 aTextureCoord2;

uniform float texelWidth;
uniform float texelHeight;

varying vec2 vTextureCoord;

varying vec2 pointATexCoord;
varying vec2 pointBTexCoord;
varying vec2 pointCTexCoord;
varying vec2 pointDTexCoord;
varying vec2 pointETexCoord;
varying vec2 pointFTexCoord;
varying vec2 pointGTexCoord;
varying vec2 pointHTexCoord;

void main() {
    gl_Position = aPosition;
    
    float tripleTexelWidth = 3.0 * texelWidth;
    float tripleTexelHeight = 3.0 * texelHeight;

    vTextureCoord = aTextureCoord.xy;
    
    pointATexCoord = vec2(aTextureCoord2.x + tripleTexelWidth, vTextureCoord.y + texelHeight);
    pointBTexCoord = vec2(aTextureCoord2.x + texelWidth, vTextureCoord.y + tripleTexelHeight);
    pointCTexCoord = vec2(aTextureCoord2.x - texelWidth, vTextureCoord.y + tripleTexelHeight);
    pointDTexCoord = vec2(aTextureCoord2.x - tripleTexelWidth, vTextureCoord.y + texelHeight);
    pointETexCoord = vec2(aTextureCoord2.x - tripleTexelWidth, vTextureCoord.y - texelHeight);
    pointFTexCoord = vec2(aTextureCoord2.x - texelWidth, vTextureCoord.y - tripleTexelHeight);
    pointGTexCoord = vec2(aTextureCoord2.x + texelWidth, vTextureCoord.y - tripleTexelHeight);
    pointHTexCoord = vec2(aTextureCoord2.x + tripleTexelWidth, vTextureCoord.y - texelHeight);
}