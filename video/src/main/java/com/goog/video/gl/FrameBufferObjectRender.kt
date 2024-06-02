package com.goog.video.gl

import android.graphics.Color
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.goog.video.filter.core.GLFilter
import java.util.LinkedList
import java.util.Queue
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

//FrameBufferObjectRenderer
abstract class FBORenderer : GLSurfaceView.Renderer {

    private val fbo = FrameBufferObject()

    //暂时不知道有什么用
    private var normalShader: GLFilter? = null

    private val runOnDraw: Queue<Runnable> = LinkedList()

    protected var mClearColor: Int = Color.TRANSPARENT

    fun setClearColor(color: Int) {
        mClearColor = color
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        normalShader = GLFilter()
        normalShader?.initialize()
        onSurfaceCreated(config)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        fbo.setup(width, height)
        normalShader?.setFrameSize(width, height)
        onSurfaceChanged(width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        synchronized(runOnDraw) {
            while (!runOnDraw.isEmpty()) {
                runOnDraw.poll()?.run()
            }
        }

        ///绑定缓冲区
        fbo.enable()
        GLES20.glViewport(0, 0, fbo.width, fbo.height)
        onDrawFrame(fbo)

        //解绑缓冲区
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)

        //二次绘制
        GLES20.glViewport(0, 0, fbo.width, fbo.height)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        normalShader?.draw(fbo.texName, null)

    }

    @Throws(Throwable::class)
    protected fun finalize() {
    }

    abstract fun onSurfaceCreated(config: EGLConfig?)

    abstract fun onSurfaceChanged(width: Int, height: Int)

    abstract fun onDrawFrame(fbo: FrameBufferObject)

}
