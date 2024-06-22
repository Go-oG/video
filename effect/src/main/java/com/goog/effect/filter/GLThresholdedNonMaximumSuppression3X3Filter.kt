package com.goog.effect.filter

import com.goog.effect.filter.core.GLConvolution3X3Filter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLThresholdedNonMaximumSuppression3X3Filter : GLConvolution3X3Filter() {
    var threshold by FloatDelegate(0.1f, 0f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("threshold", threshold)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/thresholded_non_max_suppression.fsh")
    }
}