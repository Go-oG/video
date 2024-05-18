package com.goog.video.filter.blend

import com.goog.video.filter.core.GLMultiTextureFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.checkArgs
import com.goog.video.utils.loadFilterFromAsset

class GLAlphaBlendFilter : GLMultiTextureFilter(2) {
    var mixturePercent by FloatDelegate(0.5f, 0f, 1f)

    override fun onDraw2(fbo: FrameBufferObject?) {
        put("mixturePercent", mixturePercent)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("alphaBlend.fsh")
    }

}