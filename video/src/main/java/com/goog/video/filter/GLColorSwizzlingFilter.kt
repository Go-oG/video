package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.utils.loadFilterFromAsset

class GLColorSwizzlingFilter:GLFilter() {
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("colorSwizzling.fsh")
    }
}