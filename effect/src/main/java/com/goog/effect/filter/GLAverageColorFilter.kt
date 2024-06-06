package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.utils.loadFilterFromAsset

class GLAverageColorFilter :GLFilter() {

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)

        putTextureSize()

    }
    override fun getVertexShader(): String {
        return loadFilterFromAsset("averageColor.vert")
    }
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("averageColor.fsh")
    }
}