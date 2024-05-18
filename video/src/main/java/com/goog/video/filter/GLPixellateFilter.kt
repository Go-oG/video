package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.loadFilterFromAsset

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