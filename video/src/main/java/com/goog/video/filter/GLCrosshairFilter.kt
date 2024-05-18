package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.loadFilterFromAsset

class GLCrosshairFilter:GLFilter() {
    var crosshairWidth by FloatDelegate(0.01f, 0.0f, 1.0f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("crosshairWidth",crosshairWidth)
    }
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("crosshair.fsh")
    }

    override fun getVertexShader(): String {
        return loadFilterFromAsset("crosshair.vsh")
    }
}