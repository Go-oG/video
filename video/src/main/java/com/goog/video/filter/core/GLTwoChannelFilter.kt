package com.goog.video.filter.core

import android.opengl.GLES20
import androidx.annotation.CallSuper
import com.goog.video.gl.FrameBufferObject

class GLTwoChannelFilter : GLFilter() {
    private val fbo2 = FrameBufferObject()

    override fun setFrameSize(width: Int, height: Int) {
        super.setFrameSize(width, height)
        fbo2.initialize(width, height)
    }

    override fun draw(texName: Int, fbo: FrameBufferObject?) {
        super.draw(texName, fbo)
        fbo2.enable()
    }

    @CallSuper
    override fun onDraw(fbo: FrameBufferObject?) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE8)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fbo2.texName)
        super.onDraw(fbo)
    }



}