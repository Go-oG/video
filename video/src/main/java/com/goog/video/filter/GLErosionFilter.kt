package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.Level
import com.goog.video.utils.loadFilterFromAsset

class GLErosionFilter(val level: Level=Level.L1) : GLFilter() {

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putTextureSize()
    }


    override fun getVertexShader(): String {
        if (level == Level.L1) {
            return loadFilterFromAsset("erosionDilation1.vsh")
        }
        if (level == Level.L2) {
            return loadFilterFromAsset("erosionDilation2.vsh")
        }
        if (level == Level.L3) {
            return loadFilterFromAsset("erosionDilation3.vsh")
        }
        return loadFilterFromAsset("erosionDilation4.vsh")

    }

    override fun getFragmentShader(): String {
        if (level == Level.L1) {
            return loadFilterFromAsset("erosion1.vsh")
        }
        if (level == Level.L2) {
            return loadFilterFromAsset("erosion2.vsh")
        }
        if (level == Level.L3) {
            return loadFilterFromAsset("erosion3.vsh")
        }
        return loadFilterFromAsset("erosion4.vsh")

    }
}
