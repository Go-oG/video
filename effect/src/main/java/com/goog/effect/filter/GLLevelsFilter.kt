package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.Float3
import com.goog.effect.utils.loadFilterFromAsset

class GLLevelsFilter : GLFilter() {
    var levelMinimum = Float3()
    var levelMiddle = Float3()
    var levelMaximum = Float3()
    var minOutput = Float3()
    var maxOutput = Float3()

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putVec3("levelMinimum", levelMinimum.x, levelMinimum.y, levelMinimum.z)
        putVec3("levelMiddle", levelMiddle.x, levelMiddle.y, levelMiddle.z)
        putVec3("levelMaximum", levelMaximum.x, levelMaximum.y, levelMaximum.z)
        putVec3("minOutput", minOutput.x, minOutput.y, minOutput.z)
        putVec3("maxOutput", maxOutput.x, maxOutput.y, maxOutput.z)
    }


    override fun getFragmentShader(): String {
        return loadFilterFromAsset("levels.fsh")
    }
}