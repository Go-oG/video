package com.goog.video.view.texture

import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLDisplay
import javax.microedition.khronos.egl.EGLSurface

interface EGLWindowSurfaceFactory {
    fun createWindowSurface(egl: EGL10?, display: EGLDisplay?, config: EGLConfig?,
        nativeWindow: Any?): EGLSurface?
    fun destroySurface(egl: EGL10?, display: EGLDisplay?, surface: EGLSurface?)
}

class DefaultWindowSurfaceFactory : EGLWindowSurfaceFactory {
    override fun createWindowSurface(egl: EGL10?, display: EGLDisplay?, config: EGLConfig?,
        nativeWindow: Any?): EGLSurface? {
        var result: EGLSurface? = null
        try {
            result = egl?.eglCreateWindowSurface(display, config, nativeWindow, null)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
        return result
    }

    override fun destroySurface(egl: EGL10?, display: EGLDisplay?, surface: EGLSurface?) {
        egl?.eglDestroySurface(display, surface)
    }
}
