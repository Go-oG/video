package com.goog.effect.filter

import com.goog.effect.utils.loadFilterFromAsset

class GLWeakPixelInclusionFilter : GLThreex3TextureSamplingFilter() {
    override fun getFragmentShader(): String {
       return loadFilterFromAsset("filters/weak_pixel_inclusion.frag")
    }
}
