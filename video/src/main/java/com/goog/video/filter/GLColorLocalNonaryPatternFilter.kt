package com.goog.video.filter

import com.goog.video.filter.core.GLBoxBoundFilter
import com.goog.video.utils.loadFilterFromAsset

class GLColorLocalNonaryPatternFilter: GLBoxBoundFilter(){
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("colorLocalNonaryPattern.fsh")
    }
}