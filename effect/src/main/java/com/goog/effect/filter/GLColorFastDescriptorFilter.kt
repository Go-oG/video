package com.goog.effect.filter

import com.goog.effect.filter.core.GLMultiTextureFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.utils.loadFilterFromAsset

class GLColorFastDescriptorFilter : GLMultiTextureFilter(2) {

    override fun onDraw2(fbo: FrameBufferObject?) {
        put("uPixelWidth",pixelWidth)
        put("uPixelHeight", pixelHeight)
    }


    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/color_fast_descriptor.fsh")
    }

    override fun getVertexShader(): String {
        return loadFilterFromAsset("filters/color_fast_descriptor.vsh")
    }
}