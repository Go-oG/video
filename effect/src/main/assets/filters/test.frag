precision mediump float;

varying vec2 vTextureCoord;
uniform sampler2D sTexture;

uniform bool uVertical;

uniform float uOffset;

uniform int uBlurRadius;

uniform float uWeights[31];

void main() {
    if (uBlurRadius <= 0) {
        gl_FragColor = texture2D(sTexture, vTextureCoord);
    } else {
        vec4 color = vec4(0.0);
        for (int i = -uBlurRadius; i <= uBlurRadius; i++) {
            vec2 uPosition;
            if (uVertical) {
                uPosition = vec2(vTextureCoord.x, vTextureCoord.y + float(i) * uOffset);
            } else {
                uPosition = vec2(vTextureCoord.x + float(i) * uOffset, vTextureCoord.y);
            }
            color += texture2D(sTexture, uPosition) * uWeights[abs(i)];
        }
        gl_FragColor = color/2.0;
    }

}