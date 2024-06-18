package com.goog.effect.filter

import com.goog.effect.filter.core.GLBoxBoundFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLThresholdEdgeDetectionFilter : GLBoxBoundFilter() {
    var threshold by FloatDelegate(0.1f, 0f, 1f)
    var edgeStrength by FloatDelegate(0.1f, 0f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("threshold", threshold)
        put("edgeStrength", edgeStrength)
    }


    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/threshold_edge_detection.fsh")
    }
}