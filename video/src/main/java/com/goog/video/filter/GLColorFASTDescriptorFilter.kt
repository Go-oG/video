package com.goog.video.filter

import com.goog.video.filter.core.GLMultiTextureFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.utils.loadFilterFromAsset

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