package com.goog.video.gl

import android.graphics.Color
import android.graphics.SurfaceTexture
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Surface
import com.goog.video.Player
import com.goog.video.filter.GLFilter
import com.goog.video.filter.GLPreviewFilter
import com.goog.video.utils.EGLUtil
import java.util.LinkedList
import java.util.Queue
import java.util.concurrent.atomic.AtomicBoolean
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

//FrameBufferObjectRenderer
abstract class EFBORenderer : GLSurfaceView.Renderer {

    private val fbo = EFrameBufferObject()
    private var normalShader: GLFilter? = null

    private val runOnDraw: Queue<Runnable> = LinkedList()

    protected var mClearColor: Int = Color.BLACK

    fun setClearColor(color: Int) {
        mClearColor = color
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        normalShader = GLFilter()
        normalShader?.setup()
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
        fbo.enable()

        GLES20.glViewport(0, 0, fbo.width, fbo.height)

        onDrawFrame(fbo)

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
        GLES20.glViewport(0, 0, fbo.width, fbo.height)

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        normalShader?.draw(fbo.texName, null)
    }

    @Throws(Throwable::class)
    protected fun finalize() {
    }

    abstract fun onSurfaceCreated(config: EGLConfig?)

    abstract fun onSurfaceChanged(width: Int, height: Int)

    abstract fun onDrawFrame(fbo: EFrameBufferObject?)

}
