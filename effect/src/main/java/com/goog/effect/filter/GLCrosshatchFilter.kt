package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

/**
 * 另外一种十字交叉效果
 */
class GLCrosshatchFilter : GLFilter() {
    var crossHatchSpace by FloatDelegate(0.03f, 0f)

    var lineWidth by FloatDelegate(0.003f, 0f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("uCrossHatchSpace", crossHatchSpace)
        put("uLineWidth", if (mEnable) lineWidth else 0f)
    }

    override fun setFrameSize(width: Int, height: Int) {
        super.setFrameSize(width, height)
        val singlePixelSpacing = pixelWidth
        if (crossHatchSpace < singlePixelSpacing) {
            this.crossHatchSpace = singlePixelSpacing
        }
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/crosshatch.frag")
    }
}
