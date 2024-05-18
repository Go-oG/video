package com.goog.video.filter.blend

import com.goog.video.filter.core.GLFilter
import com.goog.video.filter.core.GLMultiTextureFilter
import com.goog.video.utils.loadFilterFromAsset

class GLSourceOverBlendFilter : GLMultiTextureFilter(2) {
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("sourceOverBlend.fsh")
    }
}