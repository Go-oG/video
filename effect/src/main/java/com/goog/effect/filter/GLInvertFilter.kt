package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.utils.loadFilterFromAsset

class GLInvertFilter : GLFilter() {
    override fun getFragmentShader(): String {
       return loadFilterFromAsset("filters/invert.frag")
    }
}
