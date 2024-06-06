package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.utils.loadFilterFromAsset

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