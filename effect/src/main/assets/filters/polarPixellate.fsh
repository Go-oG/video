precision highp float;
varying highp vec2 vTextureCoord;
uniform sampler2D sTexture;

uniform highp vec2 uCenter;
uniform highp vec2 uPixelSize;

void main() {
    vec2 normCoord = 2.0 * vTextureCoord - 1.0;
    vec2 normCenter = 2.0 * uCenter - 1.0;

    normCoord -= normCenter;

    float r = length(normCoord); // to polar coords
    float phi = atan(normCoord.y, normCoord.x); // to polar coords

    r = r - mod(r, uPixelSize.x) + 0.03;
    phi = phi - mod(phi, uPixelSize.y);

    normCoord.x = r * cos(phi);
    normCoord.y = r * sin(phi);

    normCoord += normCenter;

    vec2 textureCoordinateToUse = normCoord / 2.0 + 0.5;

    gl_FragColor = texture2D(sTexture, textureCoordinateToUse);

}