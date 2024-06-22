package com.goog.effect.filter

import com.goog.effect.filter.core.GLConvolution3X3Filter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLPrewittEdgeDetection3X3Filter : GLConvolution3X3Filter() {
     var edgeStrength by FloatDelegate(1f,0f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("edgeStrength", edgeStrength)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/prewitt_edge_detection.fsh")
    }
}