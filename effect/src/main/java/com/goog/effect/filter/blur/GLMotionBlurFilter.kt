package com.goog.effect.filter.blur

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLMotionBlurFilter : GLFilter() {

    var directionalTexelStepX by FloatDelegate(0.01f)
    var directionalTexelStepY by FloatDelegate(0.01f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putVec2("directionalTexelStep",directionalTexelStepX,directionalTexelStepY)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("blur/motionBlur.fsh")
    }

    override fun getVertexShader(): String {
        return loadFilterFromAsset("blur/motionBlur.vsh")
    }
}