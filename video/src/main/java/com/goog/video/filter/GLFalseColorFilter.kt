package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FColor
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.loadFilterFromAsset

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