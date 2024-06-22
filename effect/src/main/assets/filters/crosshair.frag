precise mediump float;

varying vec2 vTextureCoord;
uniform sampler2D sTexture;

uniform vec3 uCrosshairColor;

varying vec2 centerLocation;
varying float pointSpacing;

void main() {
    vec2 distanceFromCenter = abs(centerLocation - gl_PointCoord.xy);
    float axisTest = step(pointSpacing, gl_PointCoord.y) *
    step(distanceFromCenter.x, 0.09) +
    step(pointSpacing, gl_PointCoord.x) *
    step(distanceFromCenter.y, 0.09);
    vec4 color = texture2D(sTexture, vTextureCoord);
    color.rgb = uCrosshairColor * axisTest;
    color.a = axisTest;
    gl_FragColor = color;
}