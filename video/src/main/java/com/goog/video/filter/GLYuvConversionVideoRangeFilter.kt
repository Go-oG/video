package com.goog.video.filter

import com.goog.video.filter.core.GLMultiTextureFilter
import com.goog.video.utils.loadFilterFromAsset

class GLYuvConversionVideoRangeFilter : GLMultiTextureFilter(2) {
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("yuvConversionVideoRange.fsh")
    }
}