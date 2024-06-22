package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FColor
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

//十字准线效果
class GLCrosshairFilter : GLFilter() {
    var crosshairWidth by FloatDelegate(0.01f, 0.0f, 1.0f)
    var color = FColor(1f, 1f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("uCrosshairWidth", if (mEnable) crosshairWidth else 0f)
        putColor("uCrosshairColor", color)
    }

    override fun getVertexShader(): String {
        return loadFilterFromAsset("filters/crosshair.vert")
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/crosshair.frag")
    }


}