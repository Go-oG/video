package com.goog.effect.filter.blend

import com.goog.effect.filter.core.GLMultiTextureFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLAlphaBlendFilter : GLMultiTextureFilter(2) {
    var mixturePercent by FloatDelegate(0.5f, 0f, 1f)

    override fun onDraw2(fbo: FrameBufferObject?) {
        put("mixturePercent", mixturePercent)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("alphaBlend.fsh")
    }

}