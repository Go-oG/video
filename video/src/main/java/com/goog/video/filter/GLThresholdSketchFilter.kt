package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.loadFilterFromAsset

class GLThresholdSketchFilter : GLFilter() {
    var threshold by FloatDelegate(0.1f, 0f, 1f)
    var edgeStrength by FloatDelegate(0.1f, 0f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("threshold", threshold)
        put("edgeStrength", edgeStrength)
    }


    override fun getFragmentShader(): String {
        return loadFilterFromAsset("thresholdSketch.fsh")
    }
}