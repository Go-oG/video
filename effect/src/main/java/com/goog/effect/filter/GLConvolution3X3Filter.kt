package com.goog.effect.filter

import com.goog.effect.filter.core.GLBoxBoundFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.utils.loadFilterFromAsset

class GLConvolution3X3Filter : GLBoxBoundFilter() {
    val convolutionMatrix = floatArrayOf(
        -1.0f, -1.0f, -1.0f,
        -1.0f, 8.0f, -1.0f,
        -1.0f, -1.0f, -1.0f
    )

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putMatrix3("convolutionMatrix",convolutionMatrix)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("convolution3x3.fsh")
    }
}