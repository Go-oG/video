package com.goog.video.gl

import android.opengl.GLES20
import android.util.Log
import com.goog.video.utils.EGLUtil

class FrameBufferObject {
    private val TAG="FrameBufferObject"

    var width: Int = 0
        private set

    var height: Int = 0
        private set

    private var frameBufferName = 0
    private var renderBufferName = 0

    var texName: Int = 0
        private set

    ///创建texture 并绑定到FBO
    fun setup(width: Int, height: Int) {
        val args = IntArray(1)

        ///校验纹理大小
        GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_SIZE, args, 0)
        require(!(width > args[0] || height > args[0])) { "GL_MAX_TEXTURE_SIZE " + args[0] }
        GLES20.glGetIntegerv(GLES20.GL_MAX_RENDERBUFFER_SIZE, args, 0)
        require(!(width > args[0] || height > args[0])) { "GL_MAX_RENDERBUFFER_SIZE " + args[0] }

        //获取FrameBuffer指针位置
        GLES20.glGetIntegerv(GLES20.GL_FRAMEBUFFER_BINDING, args, 0)
        val saveFrameBuffer = args[0]

        //获取RenderBuffer指针位置
        GLES20.glGetIntegerv(GLES20.GL_RENDERBUFFER_BINDING, args, 0)
        val saveRenderBuffer = args[0]

        //获取纹理指针位置
        GLES20.glGetIntegerv(GLES20.GL_TEXTURE_BINDING_2D, args, 0)
        val saveTexName = args[0]

        ///先释放以前的
        release()

        ///重新赋值
        this.width = width
        this.height = height
        try {

            ///创建一个自定义帧缓冲并将其绑定到当前上下文
            GLES20.glGenFramebuffers(1, args, 0)
            frameBufferName = args[0]
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferName)

            ///创建深度缓冲并绑定
            GLES20.glGenRenderbuffers(1, args, 0)
            renderBufferName = args[0]
            GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, renderBufferName)
            ///为深度缓冲区分配存储空间
            GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, width, height)
            ///将深度缓冲区绑定到帧缓冲区对象(frameBuffer)
            GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER, renderBufferName)

            ///创建纹理
            GLES20.glGenTextures(1, args, 0)
            if(args[0]==0){
                Log.e(TAG,"create texture fail")
            }
            texName = args[0]
            ///绑定纹理到上下文
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texName)
            ///初始化纹理相关的参数
            EGLUtil.setupTexture(GLES20.GL_TEXTURE_2D, magUseUseLinear = true, minUseLinear = false)

            ///创建一个未初始化的纹理
            ///并将texture(纹理)attach到帧缓冲区对象上(FBO)
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA,
                    GLES20.GL_UNSIGNED_BYTE, null)
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

        ///绑定FBO 和renderBuffer 以及texture
        //恢复先前绑定的帧缓冲区、渲染缓冲区和纹理对象。
        //这通常是在操作完成后，清理并恢复原来的OpenGL状态，以便不会影响后续的渲染操作
        //======================================================================================================
        //OpenGL将saveFrameBuffer作为当前环境的 帧缓冲区。所有后续的渲染操作将定向到这个帧缓冲区。
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, saveFrameBuffer)
        //OpenGL将saveRenderBuffer作为当前环境的 渲染缓冲区。渲染缓冲区通常用于深度或模板缓冲区，或颜色缓冲区
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, saveRenderBuffer)
        //OpenGL将saveTexName作为当前活动的纹理对象。所有后续的纹理操作将应用于这个纹理对象。
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
        ///启用离屏渲染
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferName)
    }
}
