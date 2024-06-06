package com.goog.effect.filter

import com.goog.effect.filter.core.GLCenterFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.utils.loadFilterFromAsset

class GLPolarPixellateFilter : GLCenterFilter() {


    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putVec2("pixelSize", width.toFloat(), height.toFloat())
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("polarPixellate.fsh")
    }
}