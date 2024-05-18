package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.utils.loadFilterFromAsset

class GLKuwaharaRadius3Filter : GLFilter() {

    override fun onDraw(fbo: FrameBufferObject?) {
        put("srcWidth",width)
        put("srcHeight",height)
    }
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("kuwaharaRadius3.fsh")
    }

}