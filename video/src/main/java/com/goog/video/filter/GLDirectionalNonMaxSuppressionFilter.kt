package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.loadFilterFromAsset

class GLDirectionalNonMaxSuppressionFilter : GLFilter() {
    var upperThreshold by FloatDelegate(1f, 0f, 1f)
    var lowerThreshold by FloatDelegate(0f, 0f, 1f)


    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putTextureSize()
        put("upperThreshold", upperThreshold)
        put("lowerThreshold", lowerThreshold)

    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("directionalNonMaxSuppression.fsh")
    }
}