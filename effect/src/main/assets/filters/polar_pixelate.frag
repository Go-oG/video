precision mediump float;
varying highp vec2 vTextureCoord;
uniform sampler2D sTexture;

uniform vec2 uCenter;
uniform vec2 uPixelSize;

void main() {
    vec2 normCoord = 2.0 * vTextureCoord - 1.0;
    vec2 normCenter = 2.0 * uCenter - 1.0;

    normCoord -= normCenter;

    float r = length(normCoord);
    float phi = atan(normCoord.y, normCoord.x);

    r = r - mod(r, uPixelSize.x) + 0.03;
    phi = phi - mod(phi, uPixelSize.y);

    normCoord.x = r * cos(phi);
    normCoord.y = r * sin(phi);
    normCoord += normCenter;

    vec2 textureCoordinateToUse = normCoord / 2.0 + 0.5;

    gl_FragColor = texture2D(sTexture, textureCoordinateToUse);

}