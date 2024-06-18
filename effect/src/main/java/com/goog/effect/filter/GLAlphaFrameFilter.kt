package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.utils.loadFilterFromAsset

class GLAlphaFrameFilter : GLFilter() {

    override fun getVertexShader(): String {
        return loadFilterFromAsset("filters/alpha_frame.vert")
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/alpha_frame.frag")
    }
}