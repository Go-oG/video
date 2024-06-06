package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLPixellateFilter : GLFilter() {

    var fractionalWidthOfPixel by FloatDelegate(1f)

    var aspectRatio by FloatDelegate(1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("aspectRatio", aspectRatio)
        put("fractionalWidthOfPixel", fractionalWidthOfPixel)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("pixellate.fsh")
    }
}