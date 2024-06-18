package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.utils.loadFilterFromAsset

class GLHistogramAccumulationFilter:GLFilter() {
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("histogram_accumulation.fsh")
    }
}