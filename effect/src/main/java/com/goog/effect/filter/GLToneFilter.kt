package com.goog.effect.filter

import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLToneFilter : GLThreex3TextureSamplingFilter() {
    var threshold by FloatDelegate(0.2f, 0f, 1f)
    var quantizationLevels by FloatDelegate(10f, 0f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("threshold", threshold)
        put("quantizationLevels", quantizationLevels)
    }

    override fun getFragmentShader(): String {
       return loadFilterFromAsset("filters/tone.frag")
    }
}
