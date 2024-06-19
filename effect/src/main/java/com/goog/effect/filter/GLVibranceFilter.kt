package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLVibranceFilter : GLFilter() {
    //suggest [-1.2,1.2]
    var vibrance by FloatDelegate(0f, -2f, 2f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("uVibrance", vibrance)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/vibrance.frag")
    }
}
