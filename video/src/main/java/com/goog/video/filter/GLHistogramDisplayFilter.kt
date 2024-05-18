package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.utils.loadFilterFromAsset

class GLHistogramDisplayFilter:GLFilter() {
    override fun getVertexShader(): String {
        return loadFilterFromAsset("histogramDisplay.vsh")
    }
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("histogramDisplay.fsh")
    }
}