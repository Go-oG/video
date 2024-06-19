package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.utils.loadFilterFromAsset

class GLSepiaFilter : GLFilter() {
    override fun getFragmentShader(): String {
       return loadFilterFromAsset("filters/sepia.frag")
    }
}
