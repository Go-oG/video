package com.goog.effect.filter.blend

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.utils.loadFilterFromAsset

class GLExclusionBlendFilter:GLFilter() {
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("exclusionBlend.fsh")
    }
}