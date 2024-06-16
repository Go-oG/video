package com.goog.effect.filter.blur

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.filter.core.GLFilterGroup
import com.goog.effect.filter.core.GLFilterGroup2
import com.goog.effect.gl.FrameBufferObject
import kotlin.math.exp
import kotlin.math.min

/**
 * 快速高斯模糊
 * 模糊半径最多支持到30
 */
class GLGaussianBlurFilter : GLFilterGroup2(listOf()) {
    init {
        mFilters = listOf(
            Blur3Inner(true),
            Blur3Inner(false)
        )
        setBlurSize(5)
    }

    fun setBlurSize(size: Int) {
        for (filter in mFilters) {
            if (filter is Blur3Inner) {
                filter.setBlurSize(min(size, 30))
            }
        }
    }

    fun setSigma(sigma: Float) {
        for (filter in mFilters) {
            if (filter is Blur3Inner) {
                filter.setSigma(sigma)
            }
        }
    }

}

private class Blur3Inner(val horizontalBlur: Boolean) : GLFilter() {
    private var sigma = 3f
    private var blurSize: Int = 0
    private var weights = floatArrayOf()

    init {
        setBlurSize(5)
    }

    fun setBlurSize(size: Int) {
        blurSize = size
        markNeedUpdateArgs()
    }

    fun setSigma(sigma: Float) {
        if (this.sigma == sigma) {
            return
        }
        this.sigma = sigma
        markNeedUpdateArgs()
    }

    override fun onUpdateArgs() {
        super.onUpdateArgs()
        weights = calculateGaussianWeights(blurSize, sigma)
    }

    override fun onDraw(fbo: FrameBufferObject?) {
        put("uBlurRadius", if (mEnable) blurSize else 0)
        putArray("uWeights", weights)
        val off = if (horizontalBlur) width else height
        put("uOffset", 1f / off)
        put("uVertical", !horizontalBlur)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying vec2 vTextureCoord;
            uniform sampler2D sTexture;
            uniform bool uVertical;
            uniform float uOffset;
            uniform int uBlurRadius;
            uniform float uWeights[31];
            
            void main() {
                if (uBlurRadius <= 0) {
                    gl_FragColor = texture2D(sTexture, vTextureCoord);
                } else {
                    vec4 color = vec4(0.0);
                    for (int i = -uBlurRadius; i <= uBlurRadius; i++) {
                        vec2 uPosition;
                        if (uVertical) {
                            uPosition = vec2(vTextureCoord.x, vTextureCoord.y + float(i) * uOffset);
                        } else {
                            uPosition = vec2(vTextureCoord.x + float(i) * uOffset, vTextureCoord.y);
                        }
                        vec4 tmpColor = texture2D(sTexture, uPosition);
                        color += tmpColor * uWeights[abs(i)];
                    }
                    gl_FragColor = color;
                }
            }
        """.trimIndent()
    }

    private fun calculateGaussianWeights(radius: Int, sigma: Float): FloatArray {
        if (radius <= 0 || sigma <= 0) {
            return floatArrayOf(0f)
        }
        val list = mutableListOf<Float>()
        var sum = 0.0f
        val sigmaPow = 2 * sigma * sigma
        for (x in 0..radius) {
            val v = exp(-(x * x) / sigmaPow)
            list.add(v)
            sum += v
        }
        sum *= 2f
        sum -= list.first()

        for (i in 0..<list.size) {
            list[i] = list[i] / sum
        }
        return list.toFloatArray()
    }

}