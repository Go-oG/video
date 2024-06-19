precision mediump float;
varying highp vec2 vTextureCoord;
uniform lowp sampler2D sTexture;
uniform mediump sampler2D toneCurveTexture;

void main() {
    lowp vec4 textureColor = texture2D(sTexture, vTextureCoord);
    lowp float redCurveValue = texture2D(toneCurveTexture, vec2(textureColor.r, 0.0)).r;
    lowp float greenCurveValue = texture2D(toneCurveTexture, vec2(textureColor.g, 0.0)).g;
    lowp float blueCurveValue = texture2D(toneCurveTexture, vec2(textureColor.b, 0.0)).b;
    gl_FragColor = vec4(redCurveValue, greenCurveValue, blueCurveValue, textureColor.a);
}