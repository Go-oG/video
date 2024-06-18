precision mediump float;
varying highp vec2 vTextureCoord;
uniform sampler2D sTexture;

uniform  vec4 circleColor;
uniform  vec4 backgroundColor;
uniform  vec2 center;
uniform  float radius;

void main() {
    float distanceFromCenter = distance(center, vTextureCoord);
    float checkForPresenceWithinCircle = step(distanceFromCenter, radius);
    gl_FragColor = mix(backgroundColor, circleColor, checkForPresenceWithinCircle);
}