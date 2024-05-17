package com.goog.video.view.texture

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.GLSurfaceView
import android.opengl.GLSurfaceView.Renderer
import android.util.AttributeSet
import android.util.Log
import android.view.TextureView
import androidx.annotation.CallSuper
import com.goog.video.gl.ISurfaceView
import com.goog.video.gl.SimpleConfigChooser
import com.goog.video.gl.SimpleContextFactory
import com.goog.video.gl.FilterRenderer
import com.goog.video.utils.ContextUtil
import com.goog.video.utils.safeInterrupt
import com.goog.video.utils.safeRun
import java.lang.ref.WeakReference
import java.util.concurrent.locks.ReentrantLock
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGL11
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLContext
import javax.microedition.khronos.egl.EGLDisplay
import javax.microedition.khronos.egl.EGLSurface
import javax.microedition.khronos.opengles.GL
import javax.microedition.khronos.opengles.GL10
import kotlin.concurrent.withLock

/**
 * copy from
 * https://github.com/appspell/ShaderView/blob/main/lib/src/main/java/com/appspell/shaderview/gl/view/GLTextureView.kt
 */
open class GLTextureView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : TextureView(context, attrs, defStyleAttr),
    TextureView.SurfaceTextureListener, ISurfaceView {

    private val sGLThreadManager = GLThreadManager()
    private val threadLock = ReentrantLock()
    private val threadLockCondition = threadLock.newCondition()

    private val mThisWeakRef = WeakReference(this)
    private var mGLThread: GLThread? = null
    private var mRenderer: Renderer? = null
    private var mDetached = false

    ///EGL config
    private var mConfigChooser: GLSurfaceView.EGLConfigChooser? = null
    private var mContextFactory: GLSurfaceView.EGLContextFactory? = null

    private var mWindowSurfaceFactory: EGLWindowSurfaceFactory? = null

    private var mGLWrapper: GLWrapper? = null
    private var mGLContextVersion = 0
    private var mKeepGLContextOnPause = false

    @Throws(Throwable::class)
    protected fun finalize() {
        safeRun {
            mGLThread?.requestExitAndWait()
        }
    }

    init {
        ContextUtil.initContext(context)
        surfaceTextureListener = this
        setRenderer(FilterRenderer(this))
    }

    fun setGLWrapper(glWrapper: GLWrapper?) {
        mGLWrapper = glWrapper
    }

    fun setKeepGLContextOnPause(keepOnPause: Boolean) {
        mKeepGLContextOnPause = keepOnPause
    }

    fun getKeepGLContextOnPause(): Boolean {
        return mKeepGLContextOnPause
    }

    fun setRenderer(renderer: Renderer?) {
        checkRenderThreadState()
        if (mConfigChooser == null) {
            mConfigChooser = SimpleConfigChooser()
        }
        if (mContextFactory == null) {
            mContextFactory = SimpleContextFactory(mGLContextVersion)
        }
        if (mWindowSurfaceFactory == null) {
            mWindowSurfaceFactory = DefaultWindowSurfaceFactory()
        }
        mRenderer = renderer
        mGLThread = GLThread(mThisWeakRef)
        mGLThread!!.start()
    }

    fun setGLContextFactory(factory: GLSurfaceView.EGLContextFactory?) {
        checkRenderThreadState()
        mContextFactory = factory
    }

    fun setGLWindowSurfaceFactory(factory: EGLWindowSurfaceFactory?) {
        checkRenderThreadState()
        mWindowSurfaceFactory = factory
    }

    fun setGLConfigChooser(configChooser: GLSurfaceView.EGLConfigChooser) {
        checkRenderThreadState()
        mConfigChooser = configChooser
    }

    fun setGLContextVersion(version: Int) {
        checkRenderThreadState()
        mGLContextVersion = version
    }

    fun setRenderMode(renderMode: Int) {
        mGLThread?.renderMode = renderMode
    }

    fun getRenderMode(): Int {
        return mGLThread!!.renderMode
    }

    override fun requestRender() {
        mGLThread?.requestRender()
    }

    override fun queueEvent(r: Runnable?) {
        mGLThread?.queueEvent(r)
    }

    open fun surfaceCreated(holder: SurfaceTexture?) {
        mGLThread?.surfaceCreated()
    }

    open fun surfaceDestroyed(holder: SurfaceTexture?) {
        mGLThread?.surfaceDestroyed()
    }

    open fun surfaceChanged(holder: SurfaceTexture?, format: Int, w: Int, h: Int) {
        mGLThread?.onWindowResize(w, h)
    }

    open fun surfaceRedrawNeededAsync(holder: SurfaceTexture?, finishDrawing: Runnable?) {
        mGLThread?.requestRenderAndNotify(finishDrawing)
    }

    @CallSuper
    fun onPause() {
        mGLThread?.onPause()
    }

    @CallSuper
    fun onResume() {
        mGLThread?.onResume()
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (mDetached && mRenderer != null) {
            var renderMode = RENDERMODE_CONTINUOUSLY
            if (mGLThread != null) {
                renderMode = mGLThread!!.renderMode
            }
            mGLThread = GLThread(mThisWeakRef)
            if (renderMode != RENDERMODE_CONTINUOUSLY) {
                mGLThread?.renderMode = renderMode
            }
            mGLThread?.start()
        }
        mDetached = false
    }

    override fun onDetachedFromWindow() {
        mGLThread?.requestExitAndWait()
        mDetached = true
        super.onDetachedFromWindow()
    }

    // ----------------------------------------------------------------------
    private inner class EglHelper(private val viewWeakRef: WeakReference<GLTextureView?>) {
        var mEgl: EGL10? = null
        var mEglDisplay: EGLDisplay? = null
        var mEglSurface: EGLSurface? = null
        var mEglConfig: EGLConfig? = null
        var mEglContext: EGLContext? = null

        fun start() {
            val tmpEgl = EGLContext.getEGL() as EGL10
            mEgl = tmpEgl
            val tmpDisplay = tmpEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY)
            mEglDisplay = tmpDisplay
            if (mEglDisplay === EGL10.EGL_NO_DISPLAY) {
                throw RuntimeException("eglGetDisplay failed")
            }

            val version = IntArray(2)
            if (!tmpEgl.eglInitialize(mEglDisplay, version)) {
                throw RuntimeException("eglInitialize failed")
            }

            val view = viewWeakRef.get()
            if (view == null) {
                mEglConfig = null
                mEglContext = null
            } else {
                val tmpConfig = view.mConfigChooser?.chooseConfig(tmpEgl, tmpDisplay)
                mEglConfig = tmpConfig
                mEglContext = view.mContextFactory?.createContext(tmpEgl, tmpDisplay, tmpConfig)
            }

            if (mEglContext == null || mEglContext === EGL10.EGL_NO_CONTEXT) {
                mEglContext = null
                LogHelper.throwEglException("createContext", mEgl?.eglGetError() ?: -1)
            }
            mEglSurface = null
        }

        fun createSurface(): Boolean {
            val tmpEgl = mEgl ?: throw RuntimeException("egl not initialized")
            val tmpDisplay = mEglDisplay ?: throw RuntimeException("eglDisplay not initialized")
            val tmpConfig = mEglConfig ?: throw RuntimeException("mEglConfig not initialized")

            destroySurfaceImp()
            val view = viewWeakRef.get()

            mEglSurface =
                view?.mWindowSurfaceFactory?.createWindowSurface(tmpEgl, tmpDisplay, tmpConfig, view.surfaceTexture)
            if (mEglSurface == null || mEglSurface === EGL10.EGL_NO_SURFACE) {
                val error = tmpEgl.eglGetError()
                if (error == EGL10.EGL_BAD_NATIVE_WINDOW) {
                    Log.e("EglHelper", "createWindowSurface returned EGL_BAD_NATIVE_WINDOW.")
                }
                return false
            }
            if (!tmpEgl.eglMakeCurrent(tmpDisplay, mEglSurface, mEglSurface, mEglContext)) {
                Log.e("EGLHelper", "eglMakeCurrent" + tmpEgl.eglGetError())
                return false
            }
            return true
        }

        fun createGL(): GL {
            var gl = mEglContext!!.gl
            val view = viewWeakRef.get()
            if (view?.mGLWrapper != null) {
                gl = view.mGLWrapper?.wrap(gl)
            }
            return gl
        }

        fun swap(): Int {
            return if (!mEgl!!.eglSwapBuffers(mEglDisplay, mEglSurface)) {
                mEgl!!.eglGetError()
            } else EGL10.EGL_SUCCESS
        }

        fun destroySurface() {
            destroySurfaceImp()
        }

        private fun destroySurfaceImp() {
            if (mEglSurface != null && mEglSurface !== EGL10.EGL_NO_SURFACE) {
                mEgl?.eglMakeCurrent(
                        mEglDisplay, EGL10.EGL_NO_SURFACE,
                        EGL10.EGL_NO_SURFACE,
                        EGL10.EGL_NO_CONTEXT)
                val view = viewWeakRef.get()
                view?.mWindowSurfaceFactory?.destroySurface(mEgl, mEglDisplay, mEglSurface)
                mEglSurface = null
            }
        }

        fun finish() {
            mEglContext?.let {
                val view = viewWeakRef.get()
                view?.mContextFactory?.destroyContext(mEgl, mEglDisplay, it)
            }
            mEglContext = null

            mEglDisplay?.let {
                mEgl?.eglTerminate(it)
            }
            mEglDisplay = null
        }

    }

    private inner class GLThread(viewWeakRef: WeakReference<GLTextureView?>) : Thread() {
        override fun run() {
            name = "GLThread $id"
            try {
                guardedRun()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                sGLThreadManager.threadExiting(this)
            }
        }

        private fun stopEglSurfaceLocked() {
            if (mHaveEglSurface) {
                mHaveEglSurface = false
                mEglHelper!!.destroySurface()
            }
        }

        private fun stopEglContextLocked() {
            if (mHaveEglContext) {
                mEglHelper!!.finish()
                mHaveEglContext = false
                sGLThreadManager.releaseEglContextLocked(this)
            }
        }

        @Throws(InterruptedException::class)
        private fun guardedRun() {
            mEglHelper = EglHelper(mGLTextureViewWeakRef)
            mHaveEglContext = false
            mHaveEglSurface = false
            mWantRenderNotification = false
            try {
                var gl: GL10? = null
                var createEglContext = false
                var createEglSurface = false
                var createGlInterface = false
                var lostEglContext = false
                var sizeChanged = false
                var wantRenderNotification = false
                var doRenderNotification = false
                var askedToReleaseEglContext = false
                var w = 0
                var h = 0
                var event: Runnable? = null
                var finishDrawingRunnable: Runnable? = null
                while (true) {
                    threadLock.withLock {
                        while (true) {
                            if (mShouldExit) {
                                return
                            }
                            if (mEventQueue.isNotEmpty()) {
                                event = mEventQueue.removeAt(0)
                                break
                            }

                            // Update the pause state.
                            var pausing = false
                            if (mPaused != mRequestPaused) {
                                pausing = mRequestPaused
                                mPaused = mRequestPaused
                                threadLockCondition.signalAll()
                            }

                            // Do we need to give up the EGL context?
                            if (mShouldReleaseEglContext) {
                                stopEglSurfaceLocked()
                                stopEglContextLocked()
                                mShouldReleaseEglContext = false
                                askedToReleaseEglContext = true
                            }

                            if (lostEglContext) {
                                stopEglSurfaceLocked()
                                stopEglContextLocked()
                                lostEglContext = false
                            }

                            if (pausing && mHaveEglSurface) {
                                stopEglSurfaceLocked()
                            }

                            // When pausing, optionally release the EGL Context:
                            if (pausing && mHaveEglContext) {
                                val view = mGLTextureViewWeakRef.get()
                                val preserveEglContextOnPause =
                                    view?.mKeepGLContextOnPause ?: false
                                if (!preserveEglContextOnPause) {
                                    stopEglContextLocked()
                                }
                            }

                            // Have we lost the SurfaceView surface?
                            if (!mHasSurface && !mWaitingForSurface) {
                                if (mHaveEglSurface) {
                                    stopEglSurfaceLocked()
                                }
                                mWaitingForSurface = true
                                mSurfaceIsBad = false
                                threadLockCondition.signalAll()
                            }

                            if (mHasSurface && mWaitingForSurface) {
                                mWaitingForSurface = false
                                threadLockCondition.signalAll()
                            }
                            if (doRenderNotification) {
                                mWantRenderNotification = false
                                doRenderNotification = false
                                mRenderComplete = true
                                threadLockCondition.signalAll()
                            }
                            if (mFinishDrawingRunnable != null) {
                                finishDrawingRunnable = mFinishDrawingRunnable
                                mFinishDrawingRunnable = null
                            }

                            if (readyToDraw()) {

                                if (!mHaveEglContext) {
                                    if (askedToReleaseEglContext) {
                                        askedToReleaseEglContext = false
                                    } else {
                                        try {
                                            mEglHelper!!.start()
                                        } catch (t: RuntimeException) {
                                            sGLThreadManager.releaseEglContextLocked(this)
                                            throw t
                                        }
                                        mHaveEglContext = true
                                        createEglContext = true
                                        threadLockCondition.signalAll()
                                    }
                                }
                                if (mHaveEglContext && !mHaveEglSurface) {
                                    mHaveEglSurface = true
                                    createEglSurface = true
                                    createGlInterface = true
                                    sizeChanged = true
                                }

                                if (mHaveEglSurface) {
                                    if (mSizeChanged) {
                                        sizeChanged = true
                                        w = mWidth
                                        h = mHeight
                                        mWantRenderNotification = true

                                        createEglSurface = true
                                        mSizeChanged = false
                                    }
                                    mRequestRender = false
                                    threadLockCondition.signalAll()
                                    if (mWantRenderNotification) {
                                        wantRenderNotification = true
                                    }
                                    break
                                }
                            } else {
                                if (finishDrawingRunnable != null) {
                                    finishDrawingRunnable!!.run()
                                    finishDrawingRunnable = null
                                }
                            }
                            threadLockCondition.await()
                        }
                    }

                    if (event != null) {
                        event!!.run()
                        event = null
                        continue
                    }
                    if (createEglSurface) {
                        if (mEglHelper!!.createSurface()) {
                            threadLock.withLock {
                                mFinishedCreatingEglSurface = true
                                threadLockCondition.signalAll()
                            }
                        } else {
                            threadLock.withLock {
                                mFinishedCreatingEglSurface = true
                                mSurfaceIsBad = true
                                threadLockCondition.signalAll()
                            }
                            continue
                        }
                        createEglSurface = false
                    }
                    if (createGlInterface) {
                        gl = mEglHelper!!.createGL() as GL10
                        createGlInterface = false
                    }
                    if (createEglContext) {
                        val view = mGLTextureViewWeakRef.get()
                        if (view != null) {
                            safeRun { view.mRenderer?.onSurfaceCreated(gl, mEglHelper!!.mEglConfig) }
                        }
                        createEglContext = false
                    }
                    if (sizeChanged) {
                        val view = mGLTextureViewWeakRef.get()
                        safeRun {
                            view?.mRenderer?.onSurfaceChanged(gl, w, h)
                        }
                        sizeChanged = false
                    }

                    run {
                        val view = mGLTextureViewWeakRef.get()
                        if (view != null) {
                            safeRun {
                                view.mRenderer?.onDrawFrame(gl)
                                finishDrawingRunnable?.run()
                                finishDrawingRunnable = null
                            }
                        }
                    }
                    when (val swapError = mEglHelper!!.swap()) {
                        EGL10.EGL_SUCCESS -> {}
                        EGL11.EGL_CONTEXT_LOST -> {
                            lostEglContext = true
                        }
                        else -> {
                            Log.e("GLThread", "eglSwapBuffers$swapError")
                            threadLock.withLock {
                                mSurfaceIsBad = true
                                threadLockCondition.signalAll()
                            }
                        }
                    }
                    if (wantRenderNotification) {
                        doRenderNotification = true
                        wantRenderNotification = false
                    }
                }
            } finally {
                threadLock.withLock {
                    stopEglSurfaceLocked()
                    stopEglContextLocked()
                }
            }
        }

        fun ableToDraw(): Boolean {
            return mHaveEglContext && mHaveEglSurface && readyToDraw()
        }

        private fun readyToDraw(): Boolean {
            return (!mPaused && mHasSurface && !mSurfaceIsBad
                    && mWidth > 0 && mHeight > 0
                    && (mRequestRender || mRenderMode == RENDERMODE_CONTINUOUSLY))
        }

        var renderMode: Int
            get() = mRenderMode
            set(renderMode) {
                require((RENDERMODE_WHEN_DIRTY <= renderMode && renderMode <= RENDERMODE_CONTINUOUSLY)) { "renderMode" }
                threadLock.withLock {
                    mRenderMode = renderMode
                    threadLockCondition.signalAll()
                }
            }

        fun requestRender() {
            threadLock.withLock {
                mRequestRender = true
                threadLockCondition.signalAll()
            }
        }

        fun requestRenderAndNotify(finishDrawing: Runnable?) {
            threadLock.withLock {
                if (currentThread() === this) {
                    return
                }
                mWantRenderNotification = true
                mRequestRender = true
                mRenderComplete = false
                mFinishDrawingRunnable = finishDrawing
                threadLockCondition.signalAll()
            }
        }

        fun surfaceCreated() {
            threadLock.withLock {
                mHasSurface = true
                mFinishedCreatingEglSurface = false
                threadLockCondition.signalAll()
                while ((mWaitingForSurface && !mFinishedCreatingEglSurface && !mExited)) {
                    safeInterrupt {
                        threadLockCondition.await()
                    }
                }
            }
        }

        fun surfaceDestroyed() {
            threadLock.withLock {
                mHasSurface = false
                threadLockCondition.signalAll()
                while ((!mWaitingForSurface) && (!mExited)) {
                    safeInterrupt { threadLockCondition.await() }
                }
            }
        }

        fun onPause() {
            threadLock.withLock {
                mRequestPaused = true
                threadLockCondition.signalAll()
                while ((!mExited) && (!mPaused)) {
                    safeInterrupt { threadLockCondition.await() }
                }
            }
        }

        fun onResume() {
            threadLock.withLock {
                mRequestPaused = false
                mRequestRender = true
                mRenderComplete = false
                threadLockCondition.signalAll()
                while ((!mExited) && mPaused && (!mRenderComplete)) {
                    safeInterrupt { threadLockCondition.await() }
                }
            }
        }

        fun onWindowResize(w: Int, h: Int) {
            threadLock.withLock {
                mWidth = w
                mHeight = h
                mSizeChanged = true
                mRequestRender = true
                mRenderComplete = false
                if (currentThread() === this) {
                    return
                }
                threadLockCondition.signalAll()

                while ((!mExited && !mPaused && !mRenderComplete && ableToDraw())) {
                    safeInterrupt { threadLockCondition.await() }
                }
            }
        }

        fun requestExitAndWait() {
            threadLock.withLock {
                mShouldExit = true
                threadLockCondition.signalAll()
                while (!mExited) {
                    safeInterrupt { threadLockCondition.await() }
                }
            }
        }

        fun requestReleaseEglContextLocked() {
            mShouldReleaseEglContext = true
            threadLockCondition.signalAll()
        }

        fun queueEvent(r: Runnable?) {
            requireNotNull(r) { "r must not be null" }
            threadLock.withLock {
                mEventQueue.add(r)
                threadLockCondition.signalAll()
            }
        }

        private var mShouldExit = false
        var mExited = false
        private var mRequestPaused = false
        private var mPaused = false
        private var mHasSurface = false
        private var mSurfaceIsBad = false
        private var mWaitingForSurface = false
        private var mHaveEglContext = false
        private var mHaveEglSurface = false
        private var mFinishedCreatingEglSurface = false
        private var mShouldReleaseEglContext = false
        private var mWidth = 0
        private var mHeight = 0
        private var mRenderMode: Int
        private var mRequestRender = true
        private var mWantRenderNotification: Boolean
        private var mRenderComplete = false
        private val mEventQueue = ArrayList<Runnable>()
        private var mSizeChanged = true
        private var mFinishDrawingRunnable: Runnable? = null

        private var mEglHelper: EglHelper? = null

        private val mGLTextureViewWeakRef: WeakReference<GLTextureView?>

        init {
            mRenderMode = RENDERMODE_CONTINUOUSLY
            mWantRenderNotification = false
            mGLTextureViewWeakRef = viewWeakRef
        }
    }

    private inner class GLThreadManager {
        @Synchronized
        fun threadExiting(thread: GLThread) {
            threadLock.withLock {
                thread.mExited = true
                threadLockCondition.signalAll()
            }
        }

        fun releaseEglContextLocked(thread: GLThread?) {
            threadLock.withLock {
                threadLockCondition.signalAll()
            }
        }
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        surfaceCreated(surface)
        surfaceChanged(surface, 0, width, height)
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        surfaceChanged(surface, 0, width, height)
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        surfaceDestroyed(surface)
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}

    private fun checkRenderThreadState() {
        check(mGLThread == null) { "setRenderer has already been called for this instance." }
    }

    companion object {
        const val RENDERMODE_WHEN_DIRTY = 0
        const val RENDERMODE_CONTINUOUSLY = 1
    }

}