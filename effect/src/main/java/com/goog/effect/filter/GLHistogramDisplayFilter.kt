package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.utils.loadFilterFromAsset

class GLHistogramDisplayFilter:GLFilter() {
    override fun getVertexShader(): String {
        return loadFilterFromAsset("histogramDisplay.vsh")
    }
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("histogramDisplay.fsh")
    }
}