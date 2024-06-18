package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLLineFilter : GLFilter() {
    var r by FloatDelegate(1f, 0f, 1f)
    var g by FloatDelegate(1f, 0f, 1f)
    var b by FloatDelegate(1f, 0f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putVec3("lineColor", r, g, b)
    }


    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/line.fsh")
    }

    override fun getVertexShader(): String {
        return loadFilterFromAsset("filters/line.vsh")
    }
}