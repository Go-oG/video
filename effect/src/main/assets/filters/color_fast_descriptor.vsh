attribute vec4 aPosition;
attribute vec4 aTextureCoord;
attribute vec4 aTextureCoord2;

uniform float uPixelWidth;
uniform float uPixelHeight;

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
    vTextureCoord = aTextureCoord.xy;

    float tripleTexelWidth = 3.0 * uPixelWidth;
    float tripleTexelHeight = 3.0 * uPixelHeight;

    pointATexCoord = vec2(aTextureCoord2.x + tripleTexelWidth, vTextureCoord.y + uPixelHeight);
    pointBTexCoord = vec2(aTextureCoord2.x + uPixelWidth, vTextureCoord.y + tripleTexelHeight);
    pointCTexCoord = vec2(aTextureCoord2.x - uPixelWidth, vTextureCoord.y + tripleTexelHeight);
    pointDTexCoord = vec2(aTextureCoord2.x - tripleTexelWidth, vTextureCoord.y + uPixelHeight);
    pointETexCoord = vec2(aTextureCoord2.x - tripleTexelWidth, vTextureCoord.y - uPixelHeight);
    pointFTexCoord = vec2(aTextureCoord2.x - uPixelWidth, vTextureCoord.y - tripleTexelHeight);
    pointGTexCoord = vec2(aTextureCoord2.x + uPixelWidth, vTextureCoord.y - tripleTexelHeight);
    pointHTexCoord = vec2(aTextureCoord2.x + tripleTexelWidth, vTextureCoord.y - uPixelHeight);
}