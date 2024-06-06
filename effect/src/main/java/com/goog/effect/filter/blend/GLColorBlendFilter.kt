package com.goog.effect.filter.blend

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.utils.loadFilterFromAsset

class GLColorBlendFilter:GLFilter() {
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("colorBlend.fsh")
    }
}