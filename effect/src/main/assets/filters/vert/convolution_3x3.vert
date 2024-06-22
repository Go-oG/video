attribute vec4 aPosition;
attribute vec4 aTextureCoord;

uniform  float uPixelWidth;
uniform  float uPixelHeight;

varying vec2 vTextureCoord;

varying vec2 leftCoord;
varying vec2 topCoord;
varying vec2 rightCoord;
varying vec2 bottomCoord;
varying vec2 leftTopCoord;
varying vec2 rightTopCoord;
varying vec2 leftBottomCoord;
varying vec2 rightBottomCoord;


void main() {
    gl_Position = aPosition;

    vec2 widthStep = vec2(uPixelWidth, 0.0);
    vec2 heightStep = vec2(0.0, uPixelHeight);
    vec2 widthHeightStep = vec2(uPixelWidth, uPixelHeight);
    vec2 widthNegativeHeightStep = vec2(uPixelWidth, -uPixelHeight);

    vTextureCoord = aTextureCoord.xy;
    leftCoord = aTextureCoord.xy - widthStep;
    rightCoord = aTextureCoord.xy + widthStep;

    topCoord = aTextureCoord.xy - heightStep;
    leftTopCoord = aTextureCoord.xy - widthHeightStep;
    rightTopCoord = aTextureCoord.xy + widthNegativeHeightStep;

    bottomCoord = aTextureCoord.xy + heightStep;
    leftBottomCoord = aTextureCoord.xy - widthNegativeHeightStep;
    rightBottomCoord = aTextureCoord.xy + widthHeightStep;
}