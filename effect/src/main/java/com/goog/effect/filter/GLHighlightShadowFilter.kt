package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLHighlightShadowFilter : GLFilter() {
    var shadows by FloatDelegate(1f, 0f, 1f)
    var highlights by FloatDelegate(0f, 0f, 1f)


    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("shadows", shadows)
        put("highlights", highlights)
    }

    override fun getFragmentShader(): String {
      return loadFilterFromAsset("filters/highlight_shadow.frag")
    }
}
