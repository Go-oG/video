package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLPolkaDotFilter : GLFilter() {

    var fractionalWidthOfPixel by FloatDelegate(1f, 0f)
    var aspectRatio by FloatDelegate(1f, 0f, includeMin = false)
    var dotScaling by FloatDelegate(0f, 0f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("fractionalWidthOfPixel", fractionalWidthOfPixel)
        put("aspectRatio", aspectRatio)
        put("dotScaling", dotScaling)
    }


    override fun getFragmentShader(): String {
        return loadFilterFromAsset("polkaDot.fsh")
    }
}