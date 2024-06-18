package com.goog.effect.filter

import com.goog.effect.filter.core.GLCenterFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLPinchDistortionFilter : GLCenterFilter() {
    var aspectRatio by FloatDelegate(0f)
    var radius by FloatDelegate(0f)
    var scale by FloatDelegate(0f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("radius", radius)
        put("aspectRatio", aspectRatio)
        put("scale", scale)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/pinch_distortion.fsh")
    }
}