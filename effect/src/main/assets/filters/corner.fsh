precision mediump float;

varying vec2 vTextureCoord;
uniform sampler2D sTexture;

// 顺序为左上，右上，右下，左下的圆角半径
uniform vec4 uCornerRadius;
// 视图的分辨率
uniform vec2 uResolution;
// DISCARD(1),
// WHITE(2),
// BLACK(3),
// TRANSPARENT(4);
uniform int uFillType;

void main() {
    vec2 texCoord = vTextureCoord * uResolution;
    vec2 resolution = uResolution;
    float radiusTL = uCornerRadius.x;
    float radiusTR = uCornerRadius.y;
    float radiusBR = uCornerRadius.z;
    float radiusBL = uCornerRadius.w;

    vec2 cornerTL = vec2(radiusTL, resolution.y - radiusTL);
    vec2 cornerTR = vec2(resolution.x - radiusTR, resolution.y - radiusTR);
    vec2 cornerBR = vec2(resolution.x - radiusBR, radiusBR);
    vec2 cornerBL = vec2(radiusBL, radiusBL);

    bool isOutside = ((texCoord.x < radiusTL && texCoord.y > resolution.y - radiusTL && length(texCoord - cornerTL) > radiusTL) ||
    (texCoord.x > resolution.x - radiusTR && texCoord.y > resolution.y - radiusTR && length(texCoord - cornerTR) > radiusTR) ||
    (texCoord.x > resolution.x - radiusBR && texCoord.y < radiusBR && length(texCoord - cornerBR) > radiusBR) ||
    (texCoord.x < radiusBL && texCoord.y < radiusBL && length(texCoord - cornerBL) > radiusBL));

    vec4 gColor = texture2D(sTexture, vTextureCoord);
    if (isOutside) {
        if (uFillType == 1) {
            discard;
            gl_FragColor = texture2D(sTexture, vTextureCoord);
        } else if (uFillType == 2) {
            gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);
        } else if (uFillType == 3) {
            gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);
        } else {
            gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);
        }
    } else {
        gl_FragColor = texture2D(sTexture, vTextureCoord);
    }
}
