package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLNobleCornerDetectorFilter : GLFilter() {
   var sensitivity by FloatDelegate(0f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("sensitivity",sensitivity)
    }
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/noble_corner_detector.fsh")
    }
}