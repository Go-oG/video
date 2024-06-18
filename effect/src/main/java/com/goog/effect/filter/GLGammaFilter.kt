package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLGammaFilter : GLFilter() {
    var gamma by FloatDelegate(1f, 0f, 4f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("gamma", gamma)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/gamma.frag")
    }
}
