package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.loadFilterFromAsset

class GLHarrisCornerDetectorFilter : GLFilter() {

    var sensitivity by FloatDelegate(1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("sensitivity", sensitivity)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("harrisCornerDetector.fsh")
    }

}