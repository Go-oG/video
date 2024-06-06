package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLColorMatrixFilter : GLFilter() {
    var colorMatrix = FloatArray(16)
    var intensity by FloatDelegate(1f, 0f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("intensity", intensity)
        putMatrix4("colorMatrix", colorMatrix)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("colorMatrix.fsh")
    }

}