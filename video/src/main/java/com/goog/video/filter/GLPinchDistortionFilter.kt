package com.goog.video.filter

import com.goog.video.filter.core.GLCenterFilter
import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.loadFilterFromAsset

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
        return loadFilterFromAsset("pinchDistortion.fsh")
    }
}