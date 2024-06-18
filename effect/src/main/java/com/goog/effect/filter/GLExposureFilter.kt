package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset


class GLExposureFilter : GLFilter() {
    var exposure by FloatDelegate(1f, 0f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        put("exposure", exposure)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/exposure.frag")
    }

}
