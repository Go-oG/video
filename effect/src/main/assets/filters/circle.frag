precision mediump float;
varying highp vec2 vTextureCoord;
uniform sampler2D sTexture;

uniform vec4 uCircleColor;
uniform vec4 uBackgroundColor;
uniform vec2 uCenter;
uniform float uRadius;

void main() {
    float distanceFromCenter = distance(uCenter, vTextureCoord);
    float checkForPresenceWithinCircle = step(distanceFromCenter, uRadius);
    gl_FragColor = mix(uBackgroundColor, uCircleColor, checkForPresenceWithinCircle);
}