package com.goog.video.filter

import com.goog.video.filter.core.GLBoxBoundFilter
import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.utils.loadFilterFromAsset

class GLXyDerivativeFilter : GLBoxBoundFilter(){

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("xyDerivative.fsh")
    }

}