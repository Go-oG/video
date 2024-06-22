package com.goog.effect.filter

import com.goog.effect.filter.core.GLConvolution3X3Filter
import com.goog.effect.utils.loadFilterFromAsset

class GLXyDerivative3X3Filter : GLConvolution3X3Filter(){

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/xy_derivative.fsh")
    }

}