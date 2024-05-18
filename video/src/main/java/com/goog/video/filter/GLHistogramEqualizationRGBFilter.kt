package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.filter.core.GLMultiTextureFilter
import com.goog.video.utils.loadFilterFromAsset

class GLHistogramEqualizationRGBFilter : GLMultiTextureFilter(2) {
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("histogramEqualizationRGB.fsh")
    }
}