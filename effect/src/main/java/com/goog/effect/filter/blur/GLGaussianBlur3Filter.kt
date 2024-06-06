package com.goog.effect.filter.blur

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.filter.core.GLFilterGroup
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.utils.checkArgs
import kotlin.math.exp

class GLGaussianBlur3Filter : GLFilterGroup(listOf()) {
    init {
        filters = mutableListOf(
                Blur3Inner(true),
                Blur3Inner(false)
        )
        setBlurSize(15)
    }

    fun setBlurSize(size: Int) {
        checkArgs(size in 1..30)
        for (filter in filters) {
            if (filter is Blur3Inner) {
                filter.setBlurSize(size)
            }
        }
    }
}

private class Blur3Inner(val horizontalBlur: Boolean) : GLFilter() {
    private val sigma = 3f
    private var blurSize: Int = 0
    private var weights = floatArrayOf()

    init {
        setBlurSize(15)
    }

    fun setBlurSize(size: Int) {
        checkArgs(size in 1..30)
        if (size == blurSize) {
            return
        }
        blurSize = size
        weights = calculateGaussianWeights(blurSize, sigma)
    }

    override fun onDraw(fbo: FrameBufferObject?) {
        if (horizontalBlur) {
            putVec2("uBlurDir", 1.0f, 0.0f)
        } else {
            putVec2("uBlurDir", 0.0f, 1.0f)
        }
        put("uBlurRadius", blurSize)
        putArray("uWeights", weights)
        val off = if (horizontalBlur) width else height
        put("uOffset", 1f / off)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;

            varying vec2 vTextureCoord;
            uniform sampler2D sTexture;

            uniform vec2 uBlurDir;
            uniform float uOffset;
            uniform int uBlurRadius;
            uniform float uWeights[31];

            void main() {
                vec4 color = vec4(0.0);
                for (int i = -uBlurRadius; i <= uBlurRadius; i++) {
                    vec2 tmp = uOffset * float(i) * uBlurDir;
                    vec4 tmpColor = texture2D(sTexture, vTextureCoord + tmp);
                    color += tmpColor * uWeights[abs(i)];
                }
                gl_FragColor = color;
            }
        """.trimIndent()
    }

    private fun calculateGaussianWeights(radius: Int, sigma: Float): FloatArray {
        var sum = 0.0f
        val list = mutableListOf<Float>()
        // 计算权重
        for (i in 0..radius) {
            list.add(exp(-(i * i) / (2.0f * sigma * sigma)))
            sum += list.last()
        }
        // 归一化权重
        for (i in 0..<list.size) {
            list[i] = list[i] / sum
        }
        return list.toFloatArray()
    }

}