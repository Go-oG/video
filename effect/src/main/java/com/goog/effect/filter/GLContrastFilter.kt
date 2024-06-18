package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

/**
 * Changes the contrast of the image.
 * contrast value ranges from 0.0 to 4.0, with 1.0 as the normal level
 */
class GLContrastFilter: GLFilter() {

    var contrast by FloatDelegate(1f, 0f, 4f)

    override fun onDraw(fbo: FrameBufferObject?) {
        put("contrast", contrast)
    }

    override fun getFragmentShader(): String {
      return loadFilterFromAsset("filters/contrast.frag")
    }

}
