package com.goog.effect.filter.blend

import com.goog.effect.filter.core.GLMultiTextureFilter
import com.goog.effect.utils.loadFilterFromAsset

class GLOverlayBlendFilter : GLMultiTextureFilter(2) {
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("overlayBlend.fsh")
    }
}