package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.model.Level
import com.goog.video.utils.loadFilterFromAsset

class GLDilationFilter(val level: Level=Level.L1) : GLFilter() {


    override fun getFragmentShader(): String {
        if (level == Level.L1) {
            return loadFilterFromAsset("dilation1.fsh")
        }
        if (level == Level.L2) {
            return loadFilterFromAsset("dilation2.fsh")
        }
        if (level == Level.L3) {
            return loadFilterFromAsset("dilation3.fsh")
        }
        return loadFilterFromAsset("dilation1.fsh")
    }
}