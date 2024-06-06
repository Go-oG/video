package com.goog.effect.filter.core

import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate

abstract class GLCenterFilter : GLFilter() {

    var centerX by FloatDelegate(0.5f, 0f, 1f)
    var centerY by FloatDelegate(0.5f, 0f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putVec2("center", centerX, centerY)
    }

}