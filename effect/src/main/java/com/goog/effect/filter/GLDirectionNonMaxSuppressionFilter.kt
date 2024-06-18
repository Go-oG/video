package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLDirectionNonMaxSuppressionFilter : GLFilter() {
    var upperThreshold by FloatDelegate(1f, 0f, 1f)
    var lowerThreshold by FloatDelegate(0f, 0f, 1f)


    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putTextureSize()
        put("upperThreshold", upperThreshold)
        put("lowerThreshold", lowerThreshold)

    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/directional_non_Max_suppression.fsh")
    }
}