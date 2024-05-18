package com.goog.video.filter

import com.goog.video.filter.core.GLBoxBoundFilter
import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.loadFilterFromAsset

class GLThresholdedNonMaximumSuppressionFilter : GLBoxBoundFilter() {
    var threshold by FloatDelegate(0.1f, 0f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("threshold", threshold)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("thresholdedNonMaximumSuppression.fsh")
    }
}