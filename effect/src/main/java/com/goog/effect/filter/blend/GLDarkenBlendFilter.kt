package com.goog.effect.filter.blend

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.utils.loadFilterFromAsset

class GLDarkenBlendFilter :GLFilter() {
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/darken_blend.fsh")
    }
}