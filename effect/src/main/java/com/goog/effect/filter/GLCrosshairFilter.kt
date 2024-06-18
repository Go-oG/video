package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLCrosshairFilter:GLFilter() {
    var crosshairWidth by FloatDelegate(0.01f, 0.0f, 1.0f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("crosshairWidth",crosshairWidth)
    }
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/crosshair.fsh")
    }

    override fun getVertexShader(): String {
        return loadFilterFromAsset("filters/crosshair.vsh")
    }
}