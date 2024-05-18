package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.loadFilterFromAsset

class GLLineFilter : GLFilter() {
    var r by FloatDelegate(1f, 0f, 1f)
    var g by FloatDelegate(1f, 0f, 1f)
    var b by FloatDelegate(1f, 0f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putVec3("lineColor", r, g, b)
    }


    override fun getFragmentShader(): String {
        return loadFilterFromAsset("line.fsh")
    }

    override fun getVertexShader(): String {
        return loadFilterFromAsset("line.vsh")
    }
}