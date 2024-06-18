package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

//TODO 效果不明确
class GLPolarPixelateFilter : GLFilter() {
    var centerX by FloatDelegate(0.5f, 0f, 1f)
    var centerY by FloatDelegate(0.5f, 0f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        putVec2("uCenter", centerX, centerY)
        putVec2("uPixelSize", 1f / width, 1f / height)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/polar_pixelate.frag")
    }
}