package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.utils.loadFilterFromAsset

class GLCGAColorSpaceFilter : GLFilter() {
    ///TODO: 参数未实现
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/cga_color_space.frag")
    }
}
