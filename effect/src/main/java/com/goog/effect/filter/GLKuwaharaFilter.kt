package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.IntDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLKuwaharaFilter : GLFilter() {
    var radius by IntDelegate(1, 0)

    override fun onDraw(fbo: FrameBufferObject?) {
        put("radius", radius)
        put("srcWidth", width)
        put("srcHeight", height)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("kuwahara.fsh")
    }
}