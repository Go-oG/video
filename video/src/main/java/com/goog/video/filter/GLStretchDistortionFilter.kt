package com.goog.video.filter

import com.goog.video.filter.core.GLCenterFilter
import com.goog.video.filter.core.GLFilter
import com.goog.video.utils.loadFilterFromAsset

class GLStretchDistortionFilter : GLCenterFilter() {

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("stretchDistortion.fsh")
    }
}