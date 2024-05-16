package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject
import com.goog.video.utils.checkArgs

class GLGaussianBlurFilter(widthOffset: Float = 0.01f, heightOffset: Float = 0.01f, blurSize: Float = 0.2f) :
    GLFilter() {

    private var texelWidthOffset: Float = 0.01f
    private var texelHeightOffset: Float = 0.01f
    private var blurSize: Float = 0.2f

    init {
        setTexelWidthOffset(widthOffset)
        setTexelHeightOffset(heightOffset)
        setBlurSize(blurSize)
    }

    fun setTexelWidthOffset(v: Float) {
        checkArgs(v >= 0.0f, "texelWidthOffset must be >= 0")
        texelWidthOffset = v
    }

    fun setTexelHeightOffset(v: Float) {
        checkArgs(v >= 0.0f, "texelHeightOffset must be >= 0")
        texelHeightOffset = v
    }

    fun setBlurSize(v: Float) {
        checkArgs(v >= 0.0f, "blurSize must be >= 0")
        blurSize = v
    }

    override fun onDraw(fbo: EFrameBufferObject?) {
        put("texelWidthOffset", texelWidthOffset)
        put("texelHeightOffset", texelHeightOffset)
        put("blurSize", blurSize)
    }

    override fun getVertexShader(): String {
        return """
            attribute vec4 aPosition;
            attribute vec4 aTextureCoord;
            const lowp int GAUSSIAN_SAMPLES = 9;
            uniform highp float texelWidthOffset;
            uniform highp float texelHeightOffset;
            uniform highp float blurSize;
            varying highp vec2 blurCoordinates[GAUSSIAN_SAMPLES];
            void main() {
                gl_Position = aPosition;
                highp vec2 vTextureCoord = aTextureCoord.xy;
                int multiplier = 0;
                highp vec2 blurStep;
                highp vec2 singleStepOffset = vec2(texelHeightOffset, texelWidthOffset) * blurSize;
                for (lowp int i = 0; i < GAUSSIAN_SAMPLES; i++) {
                    multiplier = (i - ((GAUSSIAN_SAMPLES - 1) / 2));
                    blurStep = float(multiplier) * singleStepOffset;
                    blurCoordinates[i] = vTextureCoord.xy + blurStep;
                }
            }
        """.trimIndent()
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            const lowp int GAUSSIAN_SAMPLES = 9;
            varying highp vec2 blurCoordinates[GAUSSIAN_SAMPLES];
            uniform lowp sampler2D sTexture;
            void main() {
                lowp vec4 sum = vec4(0.0);
                sum += texture2D(sTexture, blurCoordinates[0]) * 0.05;
                sum += texture2D(sTexture, blurCoordinates[1]) * 0.09;
                sum += texture2D(sTexture, blurCoordinates[2]) * 0.12;
                sum += texture2D(sTexture, blurCoordinates[3]) * 0.15;
                sum += texture2D(sTexture, blurCoordinates[4]) * 0.18;
                sum += texture2D(sTexture, blurCoordinates[5]) * 0.15;
                sum += texture2D(sTexture, blurCoordinates[6]) * 0.12;
                sum += texture2D(sTexture, blurCoordinates[7]) * 0.09;
                sum += texture2D(sTexture, blurCoordinates[8]) * 0.05;
                gl_FragColor = sum;
            }
        """.trimIndent()
    }
}
