precision mediump float;
varying highp vec2 vTextureCoord;
uniform lowp sampler2D sTexture;
uniform vec2 uCenter;
uniform float uRadius;
uniform float uScale;

void main() {
    if (uRadius <= 0.0 || uScale < 0.0) {
        gl_FragColor = texture2D(sTexture, vTextureCoord);
    } else {
        vec2 useTexCoord = vTextureCoord;
        float dist = distance(uCenter, vTextureCoord);
        useTexCoord -= uCenter;
        if (dist < uRadius) {
            float percent = 1.0 - ((uRadius - dist) / uRadius) * uScale;
            percent = percent * percent;
            useTexCoord = useTexCoord * percent;
        }
        useTexCoord += uCenter;
        gl_FragColor = texture2D(sTexture, useTexCoord);
    }
}