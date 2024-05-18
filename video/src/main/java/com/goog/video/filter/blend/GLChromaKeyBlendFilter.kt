package com.goog.video.filter.blend

import com.goog.video.filter.core.GLMultiTextureFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.utils.loadFilterFromAsset

class GLChromaKeyBlendFilter: GLMultiTextureFilter(2) {
    override fun onDraw2(fbo: FrameBufferObject?) {

    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("chromaKeyBlend.fsh")
    }
}