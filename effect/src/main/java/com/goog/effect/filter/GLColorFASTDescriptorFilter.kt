package com.goog.effect.filter

import com.goog.effect.filter.core.GLMultiTextureFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.utils.loadFilterFromAsset

class GLColorFASTDescriptorFilter : GLMultiTextureFilter(2) {

    override fun onDraw2(fbo: FrameBufferObject?) {
        put("texelWidth",1f/ width.toFloat())
        put("texelHeight", 1f/height.toFloat())
    }


    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/color_fast_descriptor.fsh")
    }

    override fun getVertexShader(): String {
        return loadFilterFromAsset("filters/color_fast_descriptor.vsh")
    }
}