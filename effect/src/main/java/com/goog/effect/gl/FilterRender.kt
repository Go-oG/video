package com.goog.effect.gl

import android.graphics.Color
import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.Matrix
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Surface
import com.goog.effect.Player
import com.goog.effect.filter.core.GLFilter
import com.goog.effect.filter.core.GLPreviewFilter
import com.goog.effect.utils.EGLUtil
import com.goog.effect.utils.TAG
import java.util.concurrent.atomic.AtomicBoolean
import javax.microedition.khronos.egl.EGLConfig

///自定义Render
class FilterRenderer(private val glSurfaceView: ISurfaceView) : FBORenderer(),
    SurfaceTexture.OnFrameAvailableListener {
    private val handler = Handler(Looper.getMainLooper())
    private var player: Player? = null

    //播放器承载的Surface
    private var playSurface: Surface? = null

    ///用于预览的纹理并实现了监听器
    private var previewTexture: ESurfaceTexture? = null

    ///控制更新
    private val updateSurfaceFlag = AtomicBoolean(false)

    ///外部纹理对应的ID或者是指针地址
    ///其类型为 GLES11Ext.GL_TEXTURE_EXTERNAL_OES
    private var externalTextureId = 0

    ///Model-View-Projection Matrix merge
    ///mvpMatrix=ProjMatrix*VMatrix*MMatrix
    ///上面顺序不能反
    private val mvpMatrix = FloatArray(16)

    //模型矩阵
    private val modelMatrix = FloatArray(16)

    //视图矩阵
    private val viewMatrix = FloatArray(16)

    //透视投影矩阵
    private val projectionMatrix = FloatArray(16)

    //变换矩阵用于进行线性变换 例如缩放平移 旋转等
    private val STMatrix = FloatArray(16)

    //用于预览
    private var previewFilter: GLPreviewFilter? = null

    ///filterFBO
    private val filterFBO = FrameBufferObject()
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

        /**
         * 通过使用[GLES11Ext.GL_TEXTURE_EXTERNAL_OES] 将视频流图像数据
         * 提供给OpenGL ES使用，在 OpenGL ES 中，使用 GL_TEXTURE_EXTERNAL_OES可以直接操作这种外部纹理，
         * 而不需要将数据复制到标准的纹理目标(如 GL_TEXTURE_2D)
         */
        val args = IntArray(1)
        GLES20.glGenTextures(1, args, 0)
        externalTextureId = args[0]

        /**
         * 创建对应的SurfaceTexture,内部是双缓冲,如果存在其它的surfaceTexture
         * 也可以调用[SurfaceTexture.attachToGLContext]来重新绑定相应纹理
         */
        val previewTexture = ESurfaceTexture(externalTextureId)
        previewTexture.setFrameAvailableListener(this)
        this.previewTexture = previewTexture
        //设置当前纹理参数
        GLES20.glBindTexture(previewTexture.textureTarget, externalTextureId)
        EGLUtil.configTexture(previewTexture.textureTarget, true, false)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)

        // GL_TEXTURE_EXTERNAL_OES
        val previewFilter = GLPreviewFilter(previewTexture.textureTarget).apply { initialize() }
        this.previewFilter = previewFilter

        ///创建一个信息surface 并进行绑定更新
        val surface = previewTexture.obtainSurface()
        handler.post {
            resetSurface(surface, true)
        }
        /**创建一个视图矩阵(viewMatrix)
         * 将相机放置在世界坐标的 (0, 0, 5) 处，即相机位于坐标系的正 Z 轴方向上，且远离原点 (0, 0, 0)
         * 相机的视线指向原点，即场景的中心点
         * 相机的上方向为世界坐标系的 Y 轴正方向
         *(0.0f, 0.0f, 5.0f)：相机的位置，即相机所在的世界坐标。
         * (0.0f, 0.0f, 0.0f)：观察点的位置，即相机的视线所指向的点的位置，通常是场景的原点。
         * (0.0f, 1.0f, 0.0f)：相机的上方向，用于确定相机的方向。
         */
        Matrix.setLookAtM(
            viewMatrix, 0,
            0.0f, 0.0f, 5.0f,
            0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
        )

        updateSurfaceFlag.set(false)

        ///确保在绘制时被初始化
        if (glFilter != null) {
            newFilterFlag.compareAndSet(false, true)
        }
    }

    override fun onSurfaceChanged(width: Int, height: Int) {
        Log.i(TAG, "onSurfaceChanged width = $width  height = $height")
        filterFBO.initialize(width, height)
        previewFilter?.setFrameSize(width, height)
        glFilter?.setFrameSize(width, height)
        aspectRatio = width.toFloat() / height

        /**
         *创建透视投影矩阵（Projection Matrix），用于将三维物体投影到二维屏幕上。
         * left, right：视锥体左右边界距离近裁剪平面的距离。
         * 使用 aspectRatio 是为了保持视图宽高比和SurfaceView宽高比一致，从而不会出现拉伸或者压缩
         *
         * bottom, top：视锥体底部顶部边界距离近裁剪平面的距离。
         *
         * near, far：近裁剪平面和远裁剪平面的距离。
         */
        Matrix.frustumM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, 5f, 7f)

        /**
         *初始化模型矩阵（Model Matrix），用于定义物体的位置、旋转和缩放。
         *这里设置为单位矩阵 则表示不经过任何变换
         */
        Matrix.setIdentityM(modelMatrix, 0)
    }

    override fun onDrawFrame(fbo: FrameBufferObject) {
        /**启用混合功能，用于实现透明或者其它效果
         * 根据源颜色的 alpha 值，以及目标颜色的 alpha 值来混合源颜色和目标颜色。
         * 下面是列举的不同参数对应的效果
         * glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)-> 常见的用于半透明效果的混合函数。
         * glBlendFunc(GL_SRC_ALPHA, GL_ONE)->通常用于实现加法混合效果
         * glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA)->通常用于实现乘法混合效果
         * glBlendFunc(GL_SRC_ALPHA, GL_DST_ALPHA)-> 通常用于实现混合两个半透明物体时的效果
         */
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)

        synchronized(this) {
            if (updateSurfaceFlag.compareAndSet(true, false)) {
                previewTexture?.updateTexImage()
                previewTexture?.getTransformMatrix(STMatrix)
            }
        }

        if (newFilterFlag.compareAndSet(true, false)) {
            glFilter?.let {
                it.initialize()
                it.setFrameSize(fbo.width, fbo.height)
            }
        }

        ///将当前渲染对象设置为指定的FBO对象并将视口设置为和FBO尺寸一致，避免拉伸
        if (glFilter != null) {
            filterFBO.enable()
            GLES20.glViewport(0, 0, filterFBO.width, filterFBO.height)
        }
        /**
         * 清除颜色缓冲区内容,这意味着之前所有绘制到屏幕上的颜色信息将会被清除并变成指定的清除颜色
         * 在渲染新的帧之前，通常需要先清除之前的渲染结果，以确保不会受到之前的绘制影响
         * 因此，这行代码通常会出现在渲染循环的开始部分
         */
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        /**
         * 将模型矩阵（Model Matrix）、视图矩阵（View Matrix）和投影矩阵（Projection Matrix）相乘，
         * 生成最终的 MVP 矩阵（Model-View-Projection Matrix
         *
         * 第一行代码将视图矩阵 viewMatrix 和模型矩阵 modelMatrix 相乘
         * 这一步通常用于将模型从模型空间转换到观察空间，即将模型相对于观察者的位置和方向进行变换。
         *
         * 第二行代码将投影矩阵 projectionMatrix 和上一步得到的结果 mvpMatrix 相乘
         * 这一步用于将观察空间中的坐标转换为裁剪空间中的坐标，同时应用透视投影，以便后续进行裁剪和最终显示。
         *
         * 在OpenGL中，通常使用 MVP 矩阵将模型从模型空间转换到裁剪空间，从而将其正确地显示在屏幕上。
         * MVP矩阵的顺序通常是先应用模型变换，然后是视图变换，最后是投影变换。这样的顺序确保了正确的坐标变换顺序，使得最终的渲染结果能够正确地显示在屏幕上。
         */
        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0)

        previewFilter?.draw(externalTextureId, mvpMatrix, STMatrix, aspectRatio)

        //绘制设置的glFilter
        glFilter?.let {
            fbo.enable()
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
            it.runTaskQueueIfNeed()
            it.draw(filterFBO.texName, fbo)
        }
    }

    override fun onFrameAvailable(previewTexture: SurfaceTexture) {
        updateSurfaceFlag.compareAndSet(false, true)
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
}

