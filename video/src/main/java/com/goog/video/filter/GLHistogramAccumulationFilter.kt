package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.utils.loadFilterFromAsset

class GLHistogramAccumulationFilter:GLFilter() {
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("histogramAccumulation.fsh")
    }
}