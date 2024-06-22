package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.checkArgs
import com.goog.effect.utils.loadFilterFromAsset

///半色调滤镜
class GLHalftoneFilter : GLFilter() {

    override fun onDraw(fbo: FrameBufferObject?) {
        put("uPixelWidth", pixelWidth)
        put("uAspectRatio", width.toFloat() / height.toFloat())
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/halftone.frag")
    }
}
