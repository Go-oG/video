package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLWhiteBalanceFilter : GLFilter() {

    var temperature by FloatDelegate(5000f, 0f)

    /// map -200 ->200
    var tint by FloatDelegate(0f, -2f, 2f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("uTemperature", temperature)
        put("uTint", tint)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/white_balance.frag")
    }

}