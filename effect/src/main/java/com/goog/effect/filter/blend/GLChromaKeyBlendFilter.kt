package com.goog.effect.filter.blend

import com.goog.effect.filter.core.GLMultiTextureFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.utils.loadFilterFromAsset

class GLChromaKeyBlendFilter: GLMultiTextureFilter(2) {
    override fun onDraw2(fbo: FrameBufferObject?) {

    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/chromaKey_blend.fsh")
    }
}