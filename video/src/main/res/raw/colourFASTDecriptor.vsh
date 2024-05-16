attribute vec4 aPosition;
attribute vec4 aTextureCoord;
attribute vec4 aTextureCoord2;

uniform float texelWidth;
uniform float texelHeight;

varying vec2 vTextureCoord;

varying vec2 pointATextureCoordinate;
varying vec2 pointBTextureCoordinate;
varying vec2 pointCTextureCoordinate;
varying vec2 pointDTextureCoordinate;
varying vec2 pointETextureCoordinate;
varying vec2 pointFTextureCoordinate;
varying vec2 pointGTextureCoordinate;
varying vec2 pointHTextureCoordinate;

void main() {
    gl_Position = position;
    
    float tripleTexelWidth = 3.0 * texelWidth;
    float tripleTexelHeight = 3.0 * texelHeight;

    vTextureCoord = aTextureCoord.xy;
    
    pointATextureCoordinate = vec2(aTextureCoord2.x + tripleTexelWidth, vTextureCoord.y + texelHeight);
    pointBTextureCoordinate = vec2(aTextureCoord2.x + texelWidth, vTextureCoord.y + tripleTexelHeight);
    pointCTextureCoordinate = vec2(aTextureCoord2.x - texelWidth, vTextureCoord.y + tripleTexelHeight);
    pointDTextureCoordinate = vec2(aTextureCoord2.x - tripleTexelWidth, vTextureCoord.y + texelHeight);
    pointETextureCoordinate = vec2(aTextureCoord2.x - tripleTexelWidth, vTextureCoord.y - texelHeight);
    pointFTextureCoordinate = vec2(aTextureCoord2.x - texelWidth, vTextureCoord.y - tripleTexelHeight);
    pointGTextureCoordinate = vec2(aTextureCoord2.x + texelWidth, vTextureCoord.y - tripleTexelHeight);
    pointHTextureCoordinate = vec2(aTextureCoord2.x + tripleTexelWidth, vTextureCoord.y - texelHeight);
}