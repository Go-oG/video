package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.model.Level
import com.goog.effect.utils.loadFilterFromAsset

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