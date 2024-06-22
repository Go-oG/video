precision mediump float;
varying vec2 vTextureCoord;
uniform sampler2D sTexture;

uniform int uKernelSize;
uniform float uKernelWeight[81];

varying vec2 vCoordOffsets[81];

void main() {
    vec4 color = vec4(0.0);
    int size = uKernelSize * uKernelSize;

    for (int i = 0; i < size; i++) {
        vec4 tmpColor = texture2D(sTexture, vTextureCoord + vCoordOffsets[i]);
        color += tmpColor * uKernelWeight[i];
    }
    gl_FragColor = color;

}