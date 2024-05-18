package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.loadFilterFromAsset

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