package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.utils.loadFilterFromAsset

class GLCGAColorSpaceFilter : GLFilter() {

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("uPixelWidth",pixelWidth)
        put("uPixelHeight",pixelHeight)

    }
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/cga_color_space.frag")
    }
}
