package com.goog.effect.filter

import com.goog.effect.filter.core.GLMultiTextureFilter
import com.goog.effect.utils.loadFilterFromAsset

class GLUnsharpMaskFilter : GLMultiTextureFilter(2) {
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("unsharpMask.fsh")
    }
}