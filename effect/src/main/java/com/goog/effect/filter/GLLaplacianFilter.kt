package com.goog.effect.filter

import com.goog.effect.filter.core.GLBoxBoundFilter
import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.utils.loadFilterFromAsset

class GLLaplacianFilter : GLBoxBoundFilter() {

    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/laplacian.frag")
    }


}