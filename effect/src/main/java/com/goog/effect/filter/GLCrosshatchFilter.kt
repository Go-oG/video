package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLCrosshatchFilter : GLFilter() {
    var crossHatchSpacing by FloatDelegate(0.03f, 0f)
    var lineWidth by FloatDelegate(0.003f, 0f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("crossHatchSpacing", crossHatchSpacing)
        put("lineWidth", lineWidth)
    }

    override fun setFrameSize(width: Int, height: Int) {
        super.setFrameSize(width, height)
        val singlePixelSpacing = if (width != 0) {
            1.0f / width.toFloat()
        } else {
            1.0f / 2048.0f
        }
        if (crossHatchSpacing < singlePixelSpacing) {
            this.crossHatchSpacing = singlePixelSpacing
        }
    }

    override fun getFragmentShader(): String {
       return loadFilterFromAsset("filters/crosshatch.frag")
    }
}
