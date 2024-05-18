package com.goog.video.filter.blend

import com.goog.video.filter.core.GLMultiTextureFilter
import com.goog.video.utils.loadFilterFromAsset

class GLHistogramEqualizationBlueFilter : GLMultiTextureFilter(2) {
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("histogramEqualizationBlue.fsh")
    }
}