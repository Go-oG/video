package com.goog.effect.filter.blend

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.utils.loadFilterFromAsset

class GLHardLightBlendFilter:GLFilter() {
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/hard_light_blend.fsh")
    }
}