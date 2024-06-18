package com.goog.effect.filter

import com.goog.effect.filter.core.GLBoxBoundFilter
import com.goog.effect.utils.loadFilterFromAsset

class GLColorLocalNonaryPatternFilter: GLBoxBoundFilter(){
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/colorLocalNonaryPattern.fsh")
    }
}