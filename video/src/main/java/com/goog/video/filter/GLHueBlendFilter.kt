package com.goog.video.filter

import com.goog.video.filter.core.GLMultiTextureFilter
import com.goog.video.utils.loadFilterFromAsset

class GLHueBlendFilter : GLMultiTextureFilter(2) {
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("hueBlend.fsh")
    }
}