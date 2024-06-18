package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.utils.loadFilterFromAsset

class GLLanczosResamplingFilter : GLFilter() {
    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putTextureSize()
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/lanczos_resampling.fsh")
    }

    override fun getVertexShader(): String {
        return loadFilterFromAsset("filters/lanczos_resampling.vsh")
    }
}