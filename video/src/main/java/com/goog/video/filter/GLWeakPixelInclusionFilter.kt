package com.goog.video.filter

class GLWeakPixelInclusionFilter : GLThreex3TextureSamplingFilter() {
    override fun getFragmentShader(): String {
        return """
            precision lowp float;
            uniform lowp sampler2D sTexture;

            varying vec2 textureCoordinate;
            varying vec2 leftTextureCoordinate;
            varying vec2 rightTextureCoordinate;
            varying vec2 topTextureCoordinate;
            varying vec2 topLeftTextureCoordinate;
            varying vec2 topRightTextureCoordinate;
            varying vec2 bottomTextureCoordinate;
            varying vec2 bottomLeftTextureCoordinate;
            varying vec2 bottomRightTextureCoordinate;

            void main() {
                float bottomLeftIntensity = texture2D(sTexture, bottomLeftTextureCoordinate).r;
                float topRightIntensity = texture2D(sTexture, topRightTextureCoordinate).r;
                float topLeftIntensity = texture2D(sTexture, topLeftTextureCoordinate).r;
                float bottomRightIntensity = texture2D(sTexture, bottomRightTextureCoordinate).r;
                float leftIntensity = texture2D(sTexture, leftTextureCoordinate).r;
                float rightIntensity = texture2D(sTexture, rightTextureCoordinate).r;
                float bottomIntensity = texture2D(sTexture, bottomTextureCoordinate).r;
                float topIntensity = texture2D(sTexture, topTextureCoordinate).r;
                float centerIntensity = texture2D(sTexture, textureCoordinate).r;
                float pixelIntensitySum = bottomLeftIntensity + topRightIntensity + topLeftIntensity + bottomRightIntensity + leftIntensity + rightIntensity + bottomIntensity + topIntensity + centerIntensity;
                float sumTest = step(1.5, pixelIntensitySum);
                float pixelTest = step(0.01, centerIntensity);
                gl_FragColor = vec4(vec3(sumTest * pixelTest), 1.0);
            }
        """.trimIndent()
    }
}
