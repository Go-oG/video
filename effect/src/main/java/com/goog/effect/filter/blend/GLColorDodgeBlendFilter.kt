package com.goog.effect.filter.blend

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.utils.loadFilterFromAsset

class GLColorDodgeBlendFilter:GLFilter() {
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/color_dodge_blend.fsh")
    }
}