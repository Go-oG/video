package com.goog.video.gl

import android.opengl.GLSurfaceView.EGLConfigChooser
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLDisplay

class SimpleConfigChooser(val redSize: Int = 8,
    val greenSize: Int = 8,
    val blueSize: Int = 8,
    val alphaSize: Int = 0,
    val depthSize: Int = 0,
    val stencilSize: Int = 0,
    val version: Int = 2) : EGLConfigChooser {

    private val configSpec: IntArray

    constructor(useRGB888: Boolean = true) : this(
            if (useRGB888) 8 else 5,
            if (useRGB888) 8 else 6,
            if (useRGB888) 8 else 5,
            0,
            0,
            0,
            2
    )

    init {
        configSpec = filterConfigSpec(intArrayOf(EGL10.EGL_RED_SIZE, redSize,
                EGL10.EGL_GREEN_SIZE, greenSize,
                EGL10.EGL_BLUE_SIZE, blueSize,
                EGL10.EGL_ALPHA_SIZE, alphaSize,
                EGL10.EGL_DEPTH_SIZE, depthSize,
                EGL10.EGL_STENCIL_SIZE, stencilSize,
                EGL10.EGL_NONE
        ), version)
    }

    private fun filterConfigSpec(configSpec: IntArray, version: Int): IntArray {
        if (version != 2) {
            return configSpec
        }

        val len = configSpec.size
        val newConfigSpec = IntArray(len + 2)
        System.arraycopy(configSpec, 0, newConfigSpec, 0, len - 1)
        newConfigSpec[len - 1] = EGL10.EGL_RENDERABLE_TYPE
        newConfigSpec[len] = EGL_OPENGL_ES2_BIT
        newConfigSpec[len + 1] = EGL10.EGL_NONE
        return newConfigSpec
    }


    override fun chooseConfig(egl: EGL10, display: EGLDisplay): EGLConfig {
        val numConfig = IntArray(1)
        require(egl.eglChooseConfig(display, configSpec, null, 0, numConfig)) { "eglChooseConfig failed" }
        val configSize = numConfig[0]
        require(configSize > 0) { "No configs match configSpec" }
        val configs = arrayOfNulls<EGLConfig>(configSize)
        require(egl.eglChooseConfig(display, configSpec, configs, configSize,
                numConfig)) { "eglChooseConfig#2 failed" }
        val config = chooseConfig(egl, display, configs) ?: throw IllegalArgumentException("No config chosen")
        return config
    }

    private fun chooseConfig(egl: EGL10, display: EGLDisplay, configs: Array<EGLConfig?>): EGLConfig? {
        for (config in configs) {
            val d = findConfigAttrib(egl, display, config, EGL10.EGL_DEPTH_SIZE)
            val s = findConfigAttrib(egl, display, config, EGL10.EGL_STENCIL_SIZE)
            if ((d >= depthSize) && (s >= stencilSize)) {
                val r = findConfigAttrib(egl, display, config, EGL10.EGL_RED_SIZE)
                val g = findConfigAttrib(egl, display, config, EGL10.EGL_GREEN_SIZE)
                val b = findConfigAttrib(egl, display, config, EGL10.EGL_BLUE_SIZE)
                val a = findConfigAttrib(egl, display, config, EGL10.EGL_ALPHA_SIZE)
                if ((r == redSize) && (g == greenSize) && (b == blueSize) && (a == alphaSize)) {
                    return config
                }
            }
        }
        return null
    }

    private fun findConfigAttrib(egl: EGL10, display: EGLDisplay, config: EGLConfig?, attribute: Int,
        defaultValue: Int = 0): Int {
        val value = IntArray(1)
        if (egl.eglGetConfigAttrib(display, config, attribute, value)) {
            return value[0]
        }
        return defaultValue
    }

    companion object {
        private const val EGL_OPENGL_ES2_BIT = 4
    }
}
