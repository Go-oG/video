precision mediump float;
const vec3 W = vec3(0.2125, 0.7154, 0.0721);
varying vec2 vTextureCoord;
uniform sampler2D sTexture;

uniform float uCrossHatchSpace;
uniform float uLineWidth;


void main() {
    float luminance = dot(texture2D(sTexture, vTextureCoord).rgb, W);
    vec4 colorToDisplay = vec4(1.0, 1.0, 1.0, 1.0);
    if (luminance < 1.00) {
        if (mod(vTextureCoord.x + vTextureCoord.y, uCrossHatchSpace) <= uLineWidth) {
            colorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);
        }
    }
    if (luminance < 0.75) {
        if (mod(vTextureCoord.x - vTextureCoord.y, uCrossHatchSpace) <= uLineWidth) {
            colorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);
        }
    }
    if (luminance < 0.50) {
        if (mod(vTextureCoord.x + vTextureCoord.y - (uCrossHatchSpace / 2.0), uCrossHatchSpace) <= uLineWidth) {
            colorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);
        }
    }
    if (luminance < 0.3) {
        if (mod(vTextureCoord.x - vTextureCoord.y - (uCrossHatchSpace / 2.0), uCrossHatchSpace) <= uLineWidth) {
            colorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);
        }
    }
    gl_FragColor = colorToDisplay;
}