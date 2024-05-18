package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.utils.loadFilterFromAsset

class GLLaplacianFilter : GLFilter() {

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putTextureSize()
    }

    override fun getVertexShader(): String {
        return loadFilterFromAsset("vertex/boxBound.vert")
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("laplacian.fsh")
    }


}