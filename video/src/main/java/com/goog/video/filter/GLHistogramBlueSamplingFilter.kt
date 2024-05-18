package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.utils.loadFilterFromAsset

 class GLHistogramBlueSamplingFilter : GLFilter() {
    override fun getVertexShader(): String {
        return loadFilterFromAsset("histogramBlueSampling.vsh")
    }
}

