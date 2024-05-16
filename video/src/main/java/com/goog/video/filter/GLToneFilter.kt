package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject
import com.goog.video.utils.checkArgs

class GLToneFilter(threshold: Float = 0.2f, quantizationLevels: Float = 10f) : GLThreex3TextureSamplingFilter() {
    private var threshold: Float = 0.2f
    private var quantizationLevels: Float = 10f

    init {
        setThreshold(threshold)
        setQuantizationLevels(quantizationLevels)
    }

    fun setThreshold(v: Float) {
        checkArgs(v in 0.0..1.0, "threshold must be >=0 and <=1")
        this.threshold = v
    }

    fun setQuantizationLevels(v: Float) {
        checkArgs(v >= 0, "quantizationLevels must be >=0")
        this.quantizationLevels = v
    }

    override fun onDraw(fbo: EFrameBufferObject?) {
        put("threshold", threshold)
        put("quantizationLevels", quantizationLevels)
    }

    override fun getFragmentShader(): String {
        return """
            precision highp float;
            const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);

            uniform lowp sampler2D sTexture;
            uniform highp float threshold;
            uniform highp float quantizationLevels;

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
                vec4 textureColor = texture2D(sTexture, textureCoordinate);
                float bottomLeftIntensity = texture2D(sTexture, bottomLeftTextureCoordinate).r;
                float topRightIntensity = texture2D(sTexture, topRightTextureCoordinate).r;
                float topLeftIntensity = texture2D(sTexture, topLeftTextureCoordinate).r;
                float bottomRightIntensity = texture2D(sTexture, bottomRightTextureCoordinate).r;
                float leftIntensity = texture2D(sTexture, leftTextureCoordinate).r;
                float rightIntensity = texture2D(sTexture, rightTextureCoordinate).r;
                float bottomIntensity = texture2D(sTexture, bottomTextureCoordinate).r;
                float topIntensity = texture2D(sTexture, topTextureCoordinate).r;
                float h = -topLeftIntensity - 2.0 * topIntensity - topRightIntensity + bottomLeftIntensity + 2.0 * bottomIntensity + bottomRightIntensity;
                float v = -bottomLeftIntensity - 2.0 * leftIntensity - topLeftIntensity + bottomRightIntensity + 2.0 * rightIntensity + topRightIntensity;
                float mag = length(vec2(h, v));
                vec3 posterizedImageColor = floor((textureColor.rgb * quantizationLevels) + 0.5) / quantizationLevels;
                float thresholdTest = 1.0 - step(threshold, mag);
                gl_FragColor = vec4(posterizedImageColor * thresholdTest, textureColor.a);
            }
        """.trimIndent()
    }
}
