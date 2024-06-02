package com.goog.video.filter.core

import android.opengl.GLES20
import com.goog.video.gl.FrameBufferObject
import com.goog.video.utils.EGLUtil

class GLTwoChannelFilter : GLFilter() {

    private var texName = 0

    override fun onInitialize() {
        super.onInitialize()
        texName = loadTexture()
    }

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE3)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texName)
    }

    fun loadTexture(): Int {
        val textures = IntArray(1)
        GLES20.glGenTextures(1, textures, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0])
        EGLUtil.setupTexture(texName)

        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA,
            width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null)
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D,
            texName, 0)

        return textures[0]
    }
}