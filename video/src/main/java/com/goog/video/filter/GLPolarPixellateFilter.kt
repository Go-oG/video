package com.goog.video.filter

import com.goog.video.filter.core.GLCenterFilter
import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.utils.loadFilterFromAsset

class GLPolarPixellateFilter : GLCenterFilter() {


    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putVec2("pixelSize", width.toFloat(), height.toFloat())
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("polarPixellate.fsh")
    }
}