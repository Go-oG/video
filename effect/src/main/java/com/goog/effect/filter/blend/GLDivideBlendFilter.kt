package com.goog.effect.filter.blend

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.utils.loadFilterFromAsset

class GLDivideBlendFilter:GLFilter() {
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/divide_blend.fsh")
    }
}