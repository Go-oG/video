package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.filter.core.GLMultiTextureFilter
import com.goog.video.utils.loadFilterFromAsset

class GLHistogramEqualizationRedFilter : GLMultiTextureFilter(2) {
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("histogramEqualizationRed.fsh")
    }
}