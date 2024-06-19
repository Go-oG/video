package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLSwirlFilter : GLFilter() {
    var angle by FloatDelegate(1f, 0f)
    var radius by FloatDelegate(0.5f, 0f, 1f)
    var centerX by FloatDelegate(0.5f, 0f, 1f)
    var centerY by FloatDelegate(0.5f, 0f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putVec2("center", centerX, centerY)
        put("radius", radius)
        put("angle", angle)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/swirl.frag")
    }
}
