package com.goog.effect.filter

import com.goog.effect.filter.core.GLMultiTextureFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.utils.loadFilterFromAsset

class GLColorFASTDescriptorFilter : GLMultiTextureFilter(2) {

    override fun onDraw2(fbo: FrameBufferObject?) {
        putTextureSize()
    }


    override fun getFragmentShader(): String {
        return loadFilterFromAsset("colorFASTDescriptor.fsh")
    }

    override fun getVertexShader(): String {
        return loadFilterFromAsset("colorFASTDescriptor.vsh")
    }
}