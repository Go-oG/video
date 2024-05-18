package com.goog.video.filter.core

import androidx.annotation.CallSuper
import com.goog.video.gl.FrameBufferObject
import com.goog.video.utils.loadFilterFromAsset

abstract class GLBoxBoundFilter : GLFilter() {

    @CallSuper
    override fun onDraw(fbo: FrameBufferObject?) {
        putTextureSize()
    }

    override fun getVertexShader(): String {
        return loadFilterFromAsset("vertex/boxBound.vert")
    }

}