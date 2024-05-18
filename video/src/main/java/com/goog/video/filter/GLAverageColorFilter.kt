package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.utils.loadFilterFromAsset

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