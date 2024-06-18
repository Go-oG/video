package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLPosterizeFilter : GLFilter() {
    var colorLevels by FloatDelegate(10f, 0f)

    override fun onDraw(fbo: FrameBufferObject?) {
        put("colorLevels", colorLevels)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/posterize.frag")
    }
}
