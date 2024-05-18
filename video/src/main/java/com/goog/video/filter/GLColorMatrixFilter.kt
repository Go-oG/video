package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.loadFilterFromAsset

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