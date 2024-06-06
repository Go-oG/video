package com.goog.effect.filter

import com.goog.effect.filter.core.GLCenterFilter
import com.goog.effect.utils.loadFilterFromAsset

class GLStretchDistortionFilter : GLCenterFilter() {

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("stretchDistortion.fsh")
    }
}