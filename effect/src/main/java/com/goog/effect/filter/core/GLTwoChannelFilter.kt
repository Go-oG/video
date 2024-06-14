package com.goog.effect.filter.core

import android.opengl.GLES20
import androidx.annotation.CallSuper
import com.goog.effect.gl.FrameBufferObject

@Deprecated("暂不可用")
class GLTwoChannelFilter : GLFilter() {
    private val fbo2 = FrameBufferObject()

    override fun setFrameSize(width: Int, height: Int) {
        super.setFrameSize(width, height)
        fbo2.initialize(width, height)
    }

    override fun draw(texName: Int, fbo: FrameBufferObject?) {
        if(!mEnable){
            return
        }
        super.draw(texName, fbo)
        fbo2.enable()
    }

    @CallSuper
    override fun onDraw(fbo: FrameBufferObject?) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE3)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fbo2.texName)
        super.onDraw(fbo)
    }



}