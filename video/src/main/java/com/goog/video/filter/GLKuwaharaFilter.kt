package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.IntDelegate
import com.goog.video.utils.checkArgs
import com.goog.video.utils.loadFilterFromAsset

class GLKuwaharaFilter : GLFilter() {
    var radius by IntDelegate(1, 0)

    override fun onDraw(fbo: FrameBufferObject?) {
        put("radius", radius)
        put("srcWidth", width)
        put("srcHeight", height)
    }

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("kuwahara.fsh")
    }
}