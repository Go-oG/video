package com.goog.effect.filter.blur

import android.util.Log
import com.goog.effect.filter.core.GLFilter
import com.goog.effect.filter.core.GLFilterGroup
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.utils.TAG
import com.goog.effect.utils.checkArgs
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.min
import kotlin.math.sqrt

class GLGaussianBlur3Filter : GLFilterGroup(listOf()) {
    init {
        filters = mutableListOf(
                Blur3Inner(true),
                Blur3Inner(false)
        )
        setBlurSize(5)
    }

    fun setBlurSize(size: Int) {
        for (filter in filters) {
            if (filter is Blur3Inner) {
                filter.setBlurSize(min(size, 30))
            }
        }
    }

}

private class Blur3Inner(val horizontalBlur: Boolean) : GLFilter() {
    private val sigma = 1f / 5
    private var blurSize: Int = 0
    private var weights = floatArrayOf()
    private val updateFlag = AtomicBoolean(true)

    init {
        setBlurSize(5)
    }

    fun setBlurSize(size: Int) {
        blurSize = size
        updateFlag.compareAndSet(false, true)
    }

    override fun onDraw(fbo: FrameBufferObject?) {
        if (updateFlag.compareAndSet(true, false)) {
            weights = calculateGaussianWeights(blurSize, blurSize / 3f)
            Log.i(TAG, "权重数组${weights.toList().toList()}")
        }

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
        for (x in -radius..radius) {
            val v = exp(-(x * x) / sigmaPow)
            list.add(v)
            sum += v
        }
        for (i in 0..<list.size) {
            list[i] = list[i] / sum
        }
        return list.subList(radius, list.size).toFloatArray()
    }

}