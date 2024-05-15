package com.goog.video.view.texture

import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGL11

internal object LogHelper {
    fun throwEglException(function: String, error: Int) {
        val message = formatEglError(function, error)
        throw RuntimeException(message)
    }

    private fun formatEglError(function: String, error: Int): String =
        function + " failed: " + getErrorString(error)

    private fun getErrorString(error: Int): String = when (error) {
        EGL10.EGL_SUCCESS -> "EGL_SUCCESS"
        EGL10.EGL_NOT_INITIALIZED -> "EGL_NOT_INITIALIZED"
        EGL10.EGL_BAD_ACCESS -> "EGL_BAD_ACCESS"
        EGL10.EGL_BAD_ALLOC -> "EGL_BAD_ALLOC"
        EGL10.EGL_BAD_ATTRIBUTE -> "EGL_BAD_ATTRIBUTE"
        EGL10.EGL_BAD_CONFIG -> "EGL_BAD_CONFIG"
        EGL10.EGL_BAD_CONTEXT -> "EGL_BAD_CONTEXT"
        EGL10.EGL_BAD_CURRENT_SURFACE -> "EGL_BAD_CURRENT_SURFACE"
        EGL10.EGL_BAD_DISPLAY -> "EGL_BAD_DISPLAY"
        EGL10.EGL_BAD_MATCH -> "EGL_BAD_MATCH"
        EGL10.EGL_BAD_NATIVE_PIXMAP -> "EGL_BAD_NATIVE_PIXMAP"
        EGL10.EGL_BAD_NATIVE_WINDOW -> "EGL_BAD_NATIVE_WINDOW"
        EGL10.EGL_BAD_PARAMETER -> "EGL_BAD_PARAMETER"
        EGL10.EGL_BAD_SURFACE -> "EGL_BAD_SURFACE"
        EGL11.EGL_CONTEXT_LOST -> "EGL_CONTEXT_LOST"
        else -> getHex(error)
    }

    private fun getHex(value: Int): String = "0x" + Integer.toHexString(value)
}