package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

/**
 * brightness value ranges from -1.0 to 1.0, with 0.0 as the normal level
 */
class GLBrightnessFilter : GLFilter() {

    var brightness by FloatDelegate(0f, -1f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        put("brightness", if (mEnable) brightness else 0.0f)
    }

    override fun getFragmentShader(): String {
       return loadFilterFromAsset("filters/brightness.frag")
    }


}
