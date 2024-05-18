package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.utils.loadFilterFromAsset

class GLLanczosResamplingFilter : GLFilter() {
    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putTextureSize()
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("lanczosResampling.fsh")
    }

    override fun getVertexShader(): String {
        return loadFilterFromAsset("lanczosResampling.vsh")
    }
}