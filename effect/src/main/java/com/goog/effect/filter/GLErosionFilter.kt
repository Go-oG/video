package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.Level
import com.goog.effect.utils.loadFilterFromAsset

class GLErosionFilter(val level: Level=Level.L1) : GLFilter() {

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putTextureSize()
    }

    override fun getVertexShader(): String {
        if (level == Level.L1) {
            return loadFilterFromAsset("filters/erosion_dilation1.vsh")
        }
        if (level == Level.L2) {
            return loadFilterFromAsset("filters/erosion_dilation2.vsh")
        }
        if (level == Level.L3) {
            return loadFilterFromAsset("filters/erosion_dilation3.vsh")
        }
        return loadFilterFromAsset("filters/erosion_dilation4.vsh")

    }

    override fun getFragmentShader(): String {
        if (level == Level.L1) {
            return loadFilterFromAsset("filters/erosion1.vsh")
        }
        if (level == Level.L2) {
            return loadFilterFromAsset("filters/erosion2.vsh")
        }
        if (level == Level.L3) {
            return loadFilterFromAsset("filters/erosion3.vsh")
        }
        return loadFilterFromAsset("filters/erosion4.vsh")

    }
}
