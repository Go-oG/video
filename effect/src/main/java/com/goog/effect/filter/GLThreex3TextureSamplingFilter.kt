package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.utils.loadFilterFromAsset

open class GLThreex3TextureSamplingFilter : GLFilter() {

    override fun onDraw(fbo: FrameBufferObject?) {
        putTextureSize()
    }

    override fun getVertexShader(): String {
        return loadFilterFromAsset("filters/three_x3.vert")
    }

    override fun getFragmentShader(): String {
        return ""
    }
}
