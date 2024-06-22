package com.goog.effect.filter.core

import android.opengl.Matrix
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.utils.checkArgs
import com.goog.effect.utils.loadFilterFromAsset
import kotlin.properties.Delegates

/**
 * 3x3卷积核
 */
abstract class GLConvolution3X3Filter : GLFilter() {

    override fun onDraw(fbo: FrameBufferObject?) {
        put("uPixelWidth", pixelWidth)
        put("uPixelHeight", pixelHeight)
    }

    override fun getVertexShader(): String {
        return loadFilterFromAsset("filters/vert/convolution_3x3.vert")
    }

}


///动态卷积
open class GLConvolutionFilter(kernelSize: Int) : GLFilter() {
    private var mKernelSize by Delegates.notNull<Int>()
    private var mKernelWeights: FloatArray

    init {
        if (kernelSize <= 0) {
            this.mKernelSize = 3
        } else if (kernelSize > 9) {
            this.mKernelSize = 9
        } else {
            this.mKernelSize = kernelSize
        }
        val kernelWeights = FloatArray(this.mKernelSize * this.mKernelSize)
        Matrix.setIdentityM(kernelWeights, 0)
        this.mKernelWeights = kernelWeights
    }

    fun setKernelWeights(weights: FloatArray) {
        checkArgs(weights.size == mKernelSize * mKernelSize)
        System.arraycopy(weights, 0, mKernelWeights, 0, weights.size)
    }

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("uPixelWidth", pixelWidth)
        put("uPixelHeight", pixelHeight)
        put("uKernelSize", mKernelSize)
        putArray("uKernelWeight", mKernelWeights)
    }

    override fun getVertexShader(): String {
        return loadFilterFromAsset("filters/dynamic_convolution.vert")
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/dynamic_convolution.frag")
    }


}