package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.utils.loadFilterFromAsset

class GLLuminanceFilter : GLFilter() {

    //TODO 待实现参数？
    override fun getFragmentShader(): String {
      return loadFilterFromAsset("filters/luminance.frag")
    }
}
