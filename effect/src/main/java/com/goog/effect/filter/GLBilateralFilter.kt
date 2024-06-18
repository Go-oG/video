package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLBilateralFilter : GLFilter() {
    var texelWidthOffset by FloatDelegate(0.003f, 0f)
    var texelHeightOffset by FloatDelegate(0.003f, 0f)
    var blurSize by FloatDelegate(1f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        put("texelWidthOffset", texelWidthOffset)
        put("texelHeightOffset", texelHeightOffset)
        put("blurSize", blurSize)
    }

    public override fun getVertexShader(): String {
        return loadFilterFromAsset("filters/bilateral.vert")
    }

    public override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/bilateral.frag")
    }

}
