package com.goog.effect.filter

import com.goog.effect.filter.core.GLBoxBoundFilter
import com.goog.effect.utils.loadFilterFromAsset

class GLXyDerivativeFilter : GLBoxBoundFilter(){

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/xy_derivative.fsh")
    }

}