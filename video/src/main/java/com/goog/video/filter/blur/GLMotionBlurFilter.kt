package com.goog.video.filter.blur

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.loadFilterFromAsset

class GLMotionBlurFilter : GLFilter() {

    var directionalTexelStepX by FloatDelegate(0.01f)
    var directionalTexelStepY by FloatDelegate(0.01f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putVec2("directionalTexelStep",directionalTexelStepX,directionalTexelStepY)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("motionBlur.fsh")
    }

    override fun getVertexShader(): String {
        return loadFilterFromAsset("motionBlur.vsh")
    }
}