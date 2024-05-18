package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.loadFilterFromAsset

class GLLuminanceRangeFilter : GLFilter() {
    var rangeReduction by FloatDelegate(0.1f)


    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("rangeReduction", rangeReduction)
    }


    override fun getFragmentShader(): String {
        return loadFilterFromAsset("luminanceRange.fsh")
    }
}