// Shader code based on Apple's CIChromaKeyFilter example: https://developer.apple.com/library/mac/#samplecode/CIChromaKeyFilter/Introduction/Intro.html

precision highp float;

varying highp vec2 vTextureCoord;
varying highp vec2 vTextureCoord2;
uniform sampler2D sTexture;
uniform sampler2D sTexture2;

uniform float thresholdSensitivity;
uniform float smoothing;
uniform vec3 colorToReplace;



void main() {
    vec4 textureColor = texture2D(sTexture, vTextureCoord);
    vec4 textureColor2 = texture2D(sTexture2, vTextureCoord2);

    float maskY = 0.2989 * colorToReplace.r + 0.5866 * colorToReplace.g + 0.1145 * colorToReplace.b;
    float maskCr = 0.7132 * (colorToReplace.r - maskY);
    float maskCb = 0.5647 * (colorToReplace.b - maskY);

    float Y = 0.2989 * textureColor.r + 0.5866 * textureColor.g + 0.1145 * textureColor.b;
    float Cr = 0.7132 * (textureColor.r - Y);
    float Cb = 0.5647 * (textureColor.b - Y);

    //     float blendValue = 1.0 - smoothstep(thresholdSensitivity - smoothing, thresholdSensitivity , abs(Cr - maskCr) + abs(Cb - maskCb));
    float blendValue = 1.0 - smoothstep(thresholdSensitivity, thresholdSensitivity + smoothing, distance(vec2(Cr, Cb), vec2(maskCr, maskCb)));
    gl_FragColor = mix(textureColor, textureColor2, blendValue);
}