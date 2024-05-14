package com.goog.video.gl

import android.opengl.EGL14.EGL_CONTEXT_CLIENT_VERSION
import android.opengl.GLSurfaceView.EGLContextFactory
import android.util.Log
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLContext
import javax.microedition.khronos.egl.EGLDisplay

class SimpleContextFactory(private val mEGLContextVersion: Int = 2) : EGLContextFactory {
    override fun createContext(egl: EGL10, display: EGLDisplay,
        config: EGLConfig): EGLContext {
        val attrList = intArrayOf(EGL_CONTEXT_CLIENT_VERSION, mEGLContextVersion, EGL10.EGL_NONE)
        return egl.eglCreateContext(display, config, EGL10.EGL_NO_CONTEXT, attrList)
    }

    override fun destroyContext(egl: EGL10, display: EGLDisplay, context: EGLContext) {
        if (!egl.eglDestroyContext(display, context)) {
            Log.e(TAG, "display:$display context: $context")
            throw RuntimeException("eglDestroyContext" + egl.eglGetError())
        }
    }

    companion object {
        private const val TAG = "EGLContextFactory"
    }
}
