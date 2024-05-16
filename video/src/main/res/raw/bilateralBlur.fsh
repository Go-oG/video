#version 100

precision mediump float;

attribute vec4 aPosition;
attribute vec4 aTextureCoord;

const int GAUSSIAN_SAMPLES = 9;

uniform float texelWidth;
uniform float texelHeight;

varying vec2 vTextureCoord;
varying vec2 blurCoordinates[GAUSSIAN_SAMPLES];

void main() {
    gl_Position = position;
    vTextureCoord = aTextureCoord.xy;

    // Calculate the positions for the blur
    int multiplier = 0;
    vec2 blurStep;
    vec2 singleStepOffset = vec2(texelWidth, texelHeight);

    for (int i = 0; i < GAUSSIAN_SAMPLES; i++) {
        multiplier = (i - ((GAUSSIAN_SAMPLES - 1) / 2));
        // Blur in x (horizontal)
        blurStep = float(multiplier) * singleStepOffset;
        blurCoordinates[i] = aTextureCoord.xy + blurStep;
    }
}