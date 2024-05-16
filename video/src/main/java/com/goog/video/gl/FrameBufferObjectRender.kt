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

    private var fbo = EFrameBufferObject()
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

class SimpleRenderer(private val glSurfaceView: ISurfaceView) : EFBORenderer(),
    SurfaceTexture.OnFrameAvailableListener {
    private val handler = Handler(Looper.getMainLooper())
    private var player: Player? = null
    private var playSurface: Surface? = null
    private var previewTexture: ESurfaceTexture? = null

    ///控制更新
    private val updateSurfaceFlag = AtomicBoolean(false)

    private var texName = 0

    private val MVPMatrix = FloatArray(16)
    private val ProjMatrix = FloatArray(16)
    private val MMatrix = FloatArray(16)
    private val VMatrix = FloatArray(16)
    private val STMatrix = FloatArray(16)

    private var filterFramebufferObject: EFrameBufferObject? = null
    private var previewFilter: GLPreviewFilter? = null
    private var glFilter: GLFilter? = null

    ///标识是否设置了新的过滤器
    private val newFilterFlag = AtomicBoolean(false)

    private var aspectRatio = 1f

    init {
        Matrix.setIdentityM(STMatrix, 0)
    }

    fun setGlFilter(filter: GLFilter?) {
        glSurfaceView.queueEvent {
            glFilter?.release()
            glFilter = filter
            newFilterFlag.set(true)
            glSurfaceView.requestRender()
        }
    }

    override fun onSurfaceCreated(config: EGLConfig?) {
        val red = Color.red(mClearColor) / 255f
        val green = Color.green(mClearColor) / 255f
        val blue = Color.blue(mClearColor) / 255f
        val alpha = Color.alpha(mClearColor) / 255F
        ///设置清屏背景色
        GLES20.glClearColor(red, green, blue, alpha)

        val args = IntArray(1)
        GLES20.glGenTextures(args.size, args, 0)
        texName = args[0]

        previewTexture = ESurfaceTexture(texName)
        previewTexture!!.setFrameAvailableListener(this)

        GLES20.glBindTexture(previewTexture!!.textureTarget, texName)

        // GL_TEXTURE_EXTERNAL_OES
        EGLUtil.setupSampler(previewTexture!!.textureTarget, GLES20.GL_LINEAR, GLES20.GL_NEAREST)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)

        filterFramebufferObject = EFrameBufferObject()

        // GL_TEXTURE_EXTERNAL_OES
        previewFilter = GLPreviewFilter(previewTexture!!.textureTarget)
        previewFilter!!.setup()

        val surface = Surface(previewTexture!!.texture)
        handler.post {
            resetSurface(surface, false)
        }

        Matrix.setLookAtM(VMatrix, 0, 0.0f, 0.0f, 5.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f)

//        synchronized(this) {
//            updateSurface = false
//        }
        updateSurfaceFlag.set(false)


        ///确保在绘制时被初始化
        if (glFilter != null) {
            newFilterFlag.set(true)
        }

        GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_SIZE, args, 0)
    }

    override fun onSurfaceChanged(width: Int, height: Int) {
        Log.d(TAG, "onSurfaceChanged width = $width  height = $height")
        filterFramebufferObject?.setup(width, height)
        previewFilter?.setFrameSize(width, height)
        glFilter?.setFrameSize(width, height)

        aspectRatio = width.toFloat() / height

        Matrix.frustumM(ProjMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, 5f, 7f)
        Matrix.setIdentityM(MMatrix, 0)
    }

    override fun onDrawFrame(fbo: EFrameBufferObject?) {
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)


        synchronized(this) {
            if (updateSurfaceFlag.compareAndSet(true, false)) {
                previewTexture?.updateTexImage()
                previewTexture?.getTransformMatrix(STMatrix)
            }
        }

        if (newFilterFlag.compareAndSet(true, false)) {
            glFilter?.setup()
            glFilter?.setFrameSize(fbo!!.width, fbo.height)
        }

        if (glFilter != null) {
            val filterFBO = filterFramebufferObject!!
            filterFBO.enable()
            GLES20.glViewport(0, 0, filterFBO.width, filterFBO.height)
        }

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        Matrix.multiplyMM(MVPMatrix, 0, VMatrix, 0, MMatrix, 0)
        Matrix.multiplyMM(MVPMatrix, 0, ProjMatrix, 0, MVPMatrix, 0)

        previewFilter?.draw(texName, MVPMatrix, STMatrix, aspectRatio)

        if (glFilter != null) {
            fbo?.enable()
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
            glFilter?.draw(filterFramebufferObject!!.texName, fbo)
        }
    }

    @Synchronized
    override fun onFrameAvailable(previewTexture: SurfaceTexture) {
        updateSurfaceFlag.set(true)
        glSurfaceView.requestRender()
    }

    fun setPlayer(player: Player?) {
        this.player = player
        resetSurface(playSurface, true)
    }

    private fun resetSurface(newSurface: Surface?, force: Boolean) {
        if (newSurface == playSurface) {
            if (force) {
                player?.setVideoSurface(newSurface)
            }
            return
        }
        val old = playSurface
        playSurface = null
        try {
            player?.setVideoSurface(null)
            old?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        playSurface = newSurface
        player?.setVideoSurface(playSurface)
    }

    ///由外界调用
    fun onPause() {
        glFilter?.release()
        previewTexture?.release()
    }

    companion object {
        private val TAG: String = SimpleRenderer::class.java.simpleName
    }

}

interface ISurfaceView {
    fun queueEvent(r: Runnable?)
    fun requestRender()
}
