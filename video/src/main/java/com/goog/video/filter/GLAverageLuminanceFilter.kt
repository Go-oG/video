package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.utils.loadFilterFromAsset

class GLAverageLuminanceFilter:GLFilter() {
    override fun getFragmentShader(): String {
        return  loadFilterFromAsset("averageLuminance.fsh")
    }
}