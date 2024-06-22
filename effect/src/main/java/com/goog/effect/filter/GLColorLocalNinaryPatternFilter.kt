package com.goog.effect.filter

import com.goog.effect.filter.core.GLConvolution3X3Filter
import com.goog.effect.utils.loadFilterFromAsset

class GLColorLocalNinaryPatternFilter: GLConvolution3X3Filter(){
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/color_local_ninary_pattern.fsh")
    }
}