package com.goog.effect.filter.blur

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

//方向模糊
class GLDirectionBlurFilter : GLFilter() {
    var directionX by FloatDelegate(0f)
    var directionY by FloatDelegate(0f)
    var blurSize by FloatDelegate(1f, 1f)

    ///不能出现负数
    private var sampleCount = 0
    private var weights = FloatArray(30)
    private var offsets = FloatArray(30)

    init {
        val weights = floatArrayOf(0.05f, 0.09f, 0.12f, 0.15f, 0.16f, 0.15f, 0.12f, 0.09f, 0.05f)
        val offsets = floatArrayOf(-4.0f, -3.0f, -2.0f, -1.0f, 0.0f, 1.0f, 2.0f, 3.0f, 4.0f)
        setWeightAndOffset(weights, offsets)
    }

    fun setWeightAndOffset(weights: FloatArray, offsets: FloatArray) {
        check(weights.size == offsets.size && weights.size <= 30)
        sampleCount = weights.size
        System.arraycopy(weights, 0, this.weights, 0, weights.size)
        System.arraycopy(offsets, 0, this.offsets, 0, offsets.size)
    }

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putVec2("uDirection", directionX, directionY)
        put("uBlurSize", blurSize)
        put("uSampleCount", sampleCount)
        putArray("uWeights", weights)
        putArray("uOffsets", offsets)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("blur/direction.frag")
    }
}