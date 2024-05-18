package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.utils.loadFilterFromAsset

class GLHistogramRedSamplingFilter : GLFilter() {
    override fun getVertexShader(): String {
        return loadFilterFromAsset("histogramRedSampling.vsh")
    }
}