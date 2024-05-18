package com.goog.video.filter.blend

import com.goog.video.filter.core.GLFilter
import com.goog.video.utils.loadFilterFromAsset

class GLColorDodgeBlendFilter:GLFilter() {
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("colorDodgeBlend.fsh")
    }
}