package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLPixelateFilter : GLFilter() {

    var pixelWidthFactor by FloatDelegate(0.02f,0f,1f)

    var aspectRatio by FloatDelegate(1f,0f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("uAspectRatio", aspectRatio)
        put("uWidthFactor", pixelWidthFactor)
    }

    override fun getFragmentShader(): String {
       return loadFilterFromAsset("filters/pixelate.frag")
    }
}