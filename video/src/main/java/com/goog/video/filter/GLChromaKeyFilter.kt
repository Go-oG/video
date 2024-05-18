package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FColor
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.loadFilterFromAsset

class GLChromaKeyFilter : GLFilter() {
    var thresholdSensitivity by FloatDelegate(0.5f, 0f, 1f)
    var smoothing by FloatDelegate(0.5f, 0f)
    var colorToReplace = FColor()

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("thresholdSensitivity", thresholdSensitivity)
        put("smoothing", smoothing)
        putColor("colorToReplace", colorToReplace)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("chromaKey.fsh")
    }
}