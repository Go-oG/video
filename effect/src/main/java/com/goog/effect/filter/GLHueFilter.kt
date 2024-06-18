package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLHueFilter : GLFilter() {
    var hue by FloatDelegate(90f, 0f, 360f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("hueAdjust", hue)
    }

    override fun getFragmentShader(): String {
     return loadFilterFromAsset("filters/hue.frag")
    }
}
