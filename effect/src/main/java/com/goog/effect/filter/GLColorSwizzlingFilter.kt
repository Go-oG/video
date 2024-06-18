package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.utils.loadFilterFromAsset

class GLColorSwizzlingFilter:GLFilter() {
    override fun getFragmentShader(): String {
       return loadFilterFromAsset("filters/color_swizzling.frag")
    }
}