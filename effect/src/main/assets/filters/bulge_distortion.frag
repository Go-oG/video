precision mediump float;
varying highp vec2 vTextureCoord;
uniform lowp sampler2D sTexture;
uniform highp vec2 center;
uniform highp float radius;
uniform highp float scale;

void main() {
    if (radius <= 0.0 || scale < 0.0) {
        gl_FragColor = texture2D(sTexture, vTextureCoord);
    } else {
        vec2 useTexCoord = vTextureCoord;
        float dist = distance(center, vTextureCoord);
        useTexCoord -= center;
        if (dist < radius) {
            float percent = 1.0 - ((radius - dist) / radius) * scale;
            percent = percent * percent;
            useTexCoord = useTexCoord * percent;
        }
        useTexCoord += center;
        gl_FragColor = texture2D(sTexture, useTexCoord);
    }
}