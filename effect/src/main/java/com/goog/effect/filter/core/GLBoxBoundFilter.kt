package com.goog.effect.filter.core

import androidx.annotation.CallSuper
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.utils.loadFilterFromAsset

abstract class GLBoxBoundFilter : GLFilter() {

    @CallSuper
    override fun onDraw(fbo: FrameBufferObject?) {
        putTextureSize()
    }

    override fun getVertexShader(): String {
        return loadFilterFromAsset("vertex/boxBound.vert")
    }

}