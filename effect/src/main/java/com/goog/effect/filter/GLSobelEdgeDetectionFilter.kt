package com.goog.effect.filter

import com.goog.effect.filter.core.GLBoxBoundFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLSobelEdgeDetectionFilter : GLBoxBoundFilter() {

    var edgeStrength by FloatDelegate(1f, 0f, 1f, includeMin = false)


    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("edgeStrength", edgeStrength)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("sobelEdgeDetection.fsh")
    }


}