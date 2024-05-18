package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.utils.loadFilterFromAsset

class GLHistogramGreenSamplingFilter : GLFilter() {
    override fun getVertexShader(): String {
        return loadFilterFromAsset("histogramGreenSampling.vsh")
    }
}