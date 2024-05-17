package com.goog.video.gl

import android.graphics.Color
import android.graphics.SurfaceTexture
import android.opengl.GLES20
import android.opengl.Matrix
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Surface
import com.goog.video.Player
import com.goog.video.filter.core.GLFilter
import com.goog.video.filter.core.GLPreviewFilter
import com.goog.video.utils.EGLUtil
import java.util.concurrent.atomic.AtomicBoolean
import javax.microedition.khronos.egl.EGLConfig

///自定义Render
class FilterRenderer(private val glSurfaceView: ISurfaceView) : FBORenderer(),
    SurfaceTexture.OnFrameAvailableListener {
    private val handler = Handler(Looper.getMainLooper())
    private var player: Player? = null
    private var playSurface: Surface? = null
    ///用于预览的纹理
    private var previewTexture: ESurfaceTexture? = null

    ///控制更新
    private val updateSurfaceFlag = AtomicBoolean(false)

    ///纹理名字
    private var texName = 0

    ///Model-View-Project Matrix merge
    ///mvpMatrix=ProjMatrix*VMatrix*MMatrix
    ///上面顺序不能反
    private val MVPMatrix = FloatArray(16)
    private val ProjMatrix = FloatArray(16)
    private val MMatrix = FloatArray(16)
    private val VMatrix = FloatArray(16)

    //未知矩阵？
    private val STMatrix = FloatArray(16)

    ///fbo
    private var filterFBO: FrameBufferObject? = null

    private var previewFilter: GLPreviewFilter? = null

    private var glFilter: GLFilter? = null

    ///标识是否设置了新的过滤器
    private val newFilterFlag = AtomicBoolean(false)

    private var aspectRatio = 1f

    init {
        ///设置为单位矩阵
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
        EGLUtil.setupTexture(previewTexture!!.textureTarget, magUseUseLinear = true, minUseLinear = false)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)

        filterFBO = FrameBufferObject()

        // GL_TEXTURE_EXTERNAL_OES
        previewFilter = GLPreviewFilter(previewTexture!!.textureTarget)
        previewFilter?.setup()

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
        filterFBO?.setup(width, height)
        previewFilter?.setFrameSize(width, height)
        glFilter?.setFrameSize(width, height)

        aspectRatio = width.toFloat() / height

        Matrix.frustumM(ProjMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, 5f, 7f)
        Matrix.setIdentityM(MMatrix, 0)
    }

    override fun onDrawFrame(fbo: FrameBufferObject) {
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
            val filterFBO = filterFBO!!
            filterFBO.enable()
            GLES20.glViewport(0, 0, filterFBO.width, filterFBO.height)
        }

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        Matrix.multiplyMM(MVPMatrix, 0, VMatrix, 0, MMatrix, 0)
        Matrix.multiplyMM(MVPMatrix, 0, ProjMatrix, 0, MVPMatrix, 0)

        previewFilter?.draw(texName, MVPMatrix, STMatrix, aspectRatio)

        if (glFilter != null) {
            fbo.enable()
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
            glFilter?.draw(filterFBO!!.texName, fbo)
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
        private val TAG: String = FilterRenderer::class.java.simpleName
    }

}

