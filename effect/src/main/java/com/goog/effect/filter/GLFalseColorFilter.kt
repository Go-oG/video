package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FColor
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLFalseColorFilter : GLFilter() {
    var intensity by FloatDelegate(0.5f, 0f, 1f)

    var firstColor = FColor()

    var secondColor = FColor()

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("intensity", intensity)
        putColor("firstColor", firstColor)
        putColor("secondColor", secondColor)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("falseColor.fsh")
    }
}