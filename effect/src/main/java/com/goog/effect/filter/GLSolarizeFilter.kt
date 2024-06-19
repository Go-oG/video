package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLSolarizeFilter : GLFilter() {
    var threshold by FloatDelegate(0.5f, 0f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        put("threshold", threshold)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/solarize.frag")
    }
}
