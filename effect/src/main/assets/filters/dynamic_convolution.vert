attribute vec4 aPosition;
attribute vec4 aTextureCoord;

uniform float uPixelWidth;
uniform float uPixelHeight;
uniform int uKernelSize;
//max=9x9
uniform float uKernelWeight[81];

varying vec2 vTextureCoord;
varying vec2 vCoordOffsets[81];

void main() {
    gl_Position = aPosition;
    vTextureCoord = aTextureCoord.xy;

    int halfSize = uKernelSize / 2;
    int index = 0;
    vec2 curCoord = aTextureCoord.xy;

    for (int x = -halfSize; x <= halfSize; x++) {
        float fx = float(x) * uPixelWidth;
        for (int y = -halfSize; y <= halfSize; y++) {
            float fy = float(y) * uPixelHeight;
            vCoordOffsets[index] = vec2(fx, fy);
            index += 1;
        }
    }

}