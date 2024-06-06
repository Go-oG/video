package com.goog.effect.filter.blur

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.utils.loadFilterFromAsset

class GLBilateralBlurFilter :GLFilter() {

    override fun getVertexShader(): String {
        return loadFilterFromAsset("blur/bilateralBlur.vsh")
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("blur/bilateralBlur.fsh")
    }
}