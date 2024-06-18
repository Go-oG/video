package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

class GLMonochromeFilter : GLFilter() {
    var intensity by FloatDelegate(1f, 0f, 1f)


    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("intensity", intensity)
    }

    //TODO 其它参数范围待确认
    override fun getFragmentShader(): String {
       return loadFilterFromAsset("filters/monochrome.frag")
    }
}
