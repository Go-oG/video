package com.goog.video.filter.blur

import com.goog.video.filter.core.GLFilter
import com.goog.video.utils.loadFilterFromAsset

class GLBilateralBlurFilter :GLFilter() {

    override fun getVertexShader(): String {
        return loadFilterFromAsset("bilateralBlur.vsh")
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("bilateralBlur.fsh")
    }
}