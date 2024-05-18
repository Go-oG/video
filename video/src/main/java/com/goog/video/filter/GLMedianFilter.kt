package com.goog.video.filter

import com.goog.video.filter.core.GLBoxBoundFilter
import com.goog.video.filter.core.GLFilter
import com.goog.video.utils.loadFilterFromAsset

class GLMedianFilter : GLBoxBoundFilter() {
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("median.fsh")
    }
}