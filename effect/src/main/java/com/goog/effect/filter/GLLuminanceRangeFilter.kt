package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLLuminanceRangeFilter : GLFilter() {
    var rangeReduction by FloatDelegate(0.1f)


    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("rangeReduction", rangeReduction)
    }


    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/luminance_range.fsh")
    }
}