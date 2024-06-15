package com.goog.effect.gl

import android.opengl.GLES20
import android.util.Log
import com.goog.effect.utils.EGLUtil
import com.goog.effect.utils.TAG
import com.goog.effect.utils.checkArgs

class FrameBufferObject {

    var width: Int = 0
        private set

    var height: Int = 0
        private set

    private var frameBufferName = 0
    private var renderBufferName = 0

    var texName: Int = 0
        private set

    /**
     * 初始化FBO相关的设置
     * 在设置完成后将会切换回原来的渲染环境
     * 在初始化后，如果需要启用该FBO 则需要调用 [enable] 方法
     */
    fun initialize(width: Int, height: Int) {
        val args = IntArray(1)

        GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_SIZE, args, 0)
        Log.i(TAG, "Max Texture Size is" + args[0])
        checkArgs(width <= args[0] && height <= args[0],
                "width($width) or height${height} must <=MaxTextureSize(${args[0]})")

        GLES20.glGetIntegerv(GLES20.GL_MAX_RENDERBUFFER_SIZE, args, 0)
        checkArgs(width <= args[0] && height <= args[0],
                "width($width) or height${height} must <=MaxRenderBufferSize(${args[0]})")

        //获取当前FrameBuffer、RenderBuffer、Texture指针位置并存储
        GLES20.glGetIntegerv(GLES20.GL_FRAMEBUFFER_BINDING, args, 0)
        val saveFrameBuffer = args[0]
        GLES20.glGetIntegerv(GLES20.GL_RENDERBUFFER_BINDING, args, 0)
        val saveRenderBuffer = args[0]
        GLES20.glGetIntegerv(GLES20.GL_TEXTURE_BINDING_2D, args, 0)
        val saveTexName = args[0]

        ///先释放以前绑定的对象并重新设置宽高
        release()

        this.width = width
        this.height = height
        try {
            ///创建一个FrameBuffer并将其绑定到当前上下文
            GLES20.glGenFramebuffers(1, args, 0)
            frameBufferName = args[0]
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferName)

            ///创建RenderBuffer并绑定到当前上下文
            GLES20.glGenRenderbuffers(1, args, 0)
            renderBufferName = args[0]
            GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, renderBufferName)

            ///创建和附加一个渲染缓冲区对象（RBO）到帧缓冲区对象（FBO）
            GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, width, height)
            GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER, renderBufferName)

            ///创建纹理
            texName = EGLUtil.createAndBindTexture(GLES20.GL_TEXTURE_2D)
            if (texName == 0) {
                Log.e(TAG, "create texture fail")
            }
            EGLUtil.configTexture(GLES20.GL_TEXTURE_2D, true, true,true)

            ///创建一个未初始化的离屏渲染纹理 将texture(纹理)attach到帧缓冲区对象上(FBO)
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA,
                    width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null)
            GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D,
                    texName, 0)

            ///校验FBO是否设置成功
            val status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER)
            if (status != GLES20.GL_FRAMEBUFFER_COMPLETE) {
                throw RuntimeException("Failed to initialize framebuffer object $status")
            }
        } catch (e: RuntimeException) {
            release()
            throw e
        }

        //======================================================================================================
        //恢复先前绑定的FrameBuffer、RenderBuffer和Texture对象
        //这样做会将渲染目标从离屏帧缓冲区切换回默认的帧缓冲区，即屏幕。
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, saveFrameBuffer)
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, saveRenderBuffer)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, saveTexName)
    }

    fun release() {
        val args = IntArray(1)
        args[0] = texName
        GLES20.glDeleteTextures(args.size, args, 0)
        texName = 0

        args[0] = renderBufferName
        GLES20.glDeleteRenderbuffers(args.size, args, 0)
        renderBufferName = 0

        args[0] = frameBufferName
        GLES20.glDeleteFramebuffers(args.size, args, 0)
        frameBufferName = 0
    }

    fun enable() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferName)
    }
}
