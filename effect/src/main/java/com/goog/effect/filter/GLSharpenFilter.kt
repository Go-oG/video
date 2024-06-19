package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLSharpenFilter : GLFilter() {

    var imageWidthFactor by FloatDelegate(0.004f, 0.000001f, 1f)
    var imageHeightFactor by FloatDelegate(0.004f, 0.00001f, 1f)

    var sharpness by FloatDelegate(0f, -4f, 4f)

    override fun onDraw(fbo: FrameBufferObject?) {
        put("imageWidthFactor", imageWidthFactor)
        put("imageHeightFactor", imageHeightFactor)
        put("sharpness", sharpness)
    }

    override fun getVertexShader(): String {
        return loadFilterFromAsset("filters/sharpen.vert")
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/sharpen.frag")
    }
}
