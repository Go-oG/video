package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.utils.loadFilterFromAsset

 class GLHistogramBlueSamplingFilter : GLFilter() {
    override fun getVertexShader(): String {
        return loadFilterFromAsset("histogramBlueSampling.vsh")
    }
}

