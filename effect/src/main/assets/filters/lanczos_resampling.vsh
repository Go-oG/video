attribute vec4 aPosition;
attribute vec4 aTextureCoord;

uniform float texelWidth;
uniform float texelHeight;

varying vec2 vTextureCoord;

varying vec2 oneStepLeftTexCoord;
varying vec2 twoStepsLeftTexCoord;
varying vec2 threeStepsLeftTexCoord;
varying vec2 fourStepsLeftTexCoord;
varying vec2 oneStepRightTexCoord;
varying vec2 twoStepsRightTexCoord;
varying vec2 threeStepsRightTexCoord;
varying vec2 fourStepsRightTexCoord;

void main() {
    gl_Position = aPosition;
    
    vec2 firstOffset = vec2(texelWidth, texelHeight);
    vec2 secondOffset = vec2(2.0 * texelWidth, 2.0 * texelHeight);
    vec2 thirdOffset = vec2(3.0 * texelWidth, 3.0 * texelHeight);
    vec2 fourthOffset = vec2(4.0 * texelWidth, 4.0 * texelHeight);

    vTextureCoord = aTextureCoord.xy;
    oneStepLeftTexCoord = aTextureCoord.xy - firstOffset;
    twoStepsLeftTexCoord = aTextureCoord.xy - secondOffset;
    threeStepsLeftTexCoord = aTextureCoord.xy - thirdOffset;
    fourStepsLeftTexCoord = aTextureCoord.xy - fourthOffset;
    oneStepRightTexCoord = aTextureCoord.xy + firstOffset;
    twoStepsRightTexCoord = aTextureCoord.xy + secondOffset;
    threeStepsRightTexCoord = aTextureCoord.xy + thirdOffset;
    fourStepsRightTexCoord = aTextureCoord.xy + fourthOffset;
}