package com.goog.effect.filter.blur

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.model.IntDelegate
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.exp
import kotlin.math.max

///优化后的高斯模糊
///具有更好的模糊质量和可控的参数
class GLGaussianBlur2Filter : GLFilter() {
    private var mSampleFactor by FloatDelegate(8f, 1f)
    private var mSigma by FloatDelegate(3f, 1f)
    private var mBlurRadius by IntDelegate(15, 1, 30)

    private val needComputeFlag = AtomicBoolean(true)
    private var weights = floatArrayOf()
    private var offsets = floatArrayOf()

    fun setBlurRadius(size: Int) {
        val ss = if (size % 2 == 0) {
            size + 1
        } else {
            size
        }
        if (ss == mBlurRadius) {
            return
        }
        mBlurRadius = ss
        needComputeFlag.set(true)
    }

    fun setSigma(v: Float) {
        if (mSigma == v) {
            return
        }
        mSigma = v
        needComputeFlag.set(true)
    }

    fun setSampleFactor(sampleFactor: Float) {
        if (sampleFactor == mSampleFactor) {
            return
        }
        mSampleFactor = sampleFactor
        needComputeFlag.set(true)
    }

    override fun setFrameSize(width: Int, height: Int) {
        super.setFrameSize(width, height)
        needComputeFlag.set(true)
    }

    override fun onDraw(fbo: FrameBufferObject?) {
        val w = width.toFloat()
        val h = height.toFloat()
        computeIfNeed(w, h, mBlurRadius, mSigma)
        put("uBlurRadius", mBlurRadius)
        putArray("uWeights", weights)
        putArray("uOffsets", offsets)
    }

    override fun getFragmentShader(): String {
        return """
            varying highp vec2 vTextureCoord;
            uniform lowp sampler2D sTexture;

            uniform int uBlurRadius;
            uniform float uWeights[31];
            uniform float uOffsets[31];

            void main() {
                float total = 0.0;
                vec3 ret = vec3(0.0);
                for (int iy = 0; iy < uBlurRadius; ++iy) {
                    float offsetY = uOffsets[iy];
                    float yWeight = uWeights[iy];
                    for (int ix = 0; ix < uBlurRadius; ++ix) {
                        float offsetX = uOffsets[ix];
                        float xWeight = uWeights[ix];
                        float vv = xWeight * yWeight;
                        total += vv;
                        ret += texture2D(sTexture, vTextureCoord + vec2(offsetX, offsetY)).rgb * vv;
                    }
                }
                gl_FragColor = vec4(ret / total, 1.0);
            }
        """.trimIndent()
    }

    private fun computeIfNeed(w: Float, h: Float, blurRadius: Int, sigma: Float) {
        if (needComputeFlag.compareAndSet(true, false)) {
            weights = computeWeight(blurRadius, sigma)
            offsets = computeOffset(blurRadius, sigma, w, h)
        }
    }

    private fun computeWeight(blurRadius: Int, sigma: Float): FloatArray {
        val halfSamples = blurRadius / 2
        val powSigma = 2f * sigma * sigma
        val list = FloatArray(blurRadius)
        for (i in 0..<blurRadius) {
            val offset = i - halfSamples
            list[i] = (exp(-offset * offset / powSigma))
        }
        return list
    }

    private fun computeOffset(blurRadius: Int, sigma: Float, w: Float, h: Float): FloatArray {
        val list = FloatArray(blurRadius)
        val halfSamples = sigma / 2f

        val pixelSize = 1f / (max(w, h) / mSampleFactor)

        for (i in 0..<blurRadius) {
            list[i] = (i - halfSamples) * pixelSize
        }
        return list
    }

}