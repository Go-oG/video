package com.goog.video.gl

import android.graphics.PixelFormat
import android.opengl.GLSurfaceView.EGLConfigChooser
import com.goog.video.utils.checkArgs
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLDisplay

///默认为RGBA_8888 8，8，8，8，16，0
///RGB_565 对应为 5，6，5，0，0，0
class SimpleConfigChooser private constructor(
    val redSize: Int,
    val greenSize: Int,
    val blueSize: Int,
    val alphaSize: Int,
    val depthSize: Int,
    val stencilSize: Int,
    val version: Int) : EGLConfigChooser {

    companion object {
        private const val EGL_OPENGL_ES2_BIT = 4

        fun RGBA8888(version: Int = 2): SimpleConfigChooser {
            return SimpleConfigChooser(8, 8, 8, 8, 16, 0, version)
        }

        fun RGB565(version: Int = 2): SimpleConfigChooser {
            return SimpleConfigChooser(5, 6, 5, 0, 16, 0, version)
        }

        fun RGB888(version: Int = 2): SimpleConfigChooser {
            return SimpleConfigChooser(8, 8, 8, 0, 16, 0, version)
        }

    }

    private val configSpec: IntArray

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

    ///only support RGB_565 and RGBA_8888 or RGB_888
    fun getPixelFormat(): Int {
        val b1 = redSize == greenSize && redSize == blueSize
        if (b1) {
            checkArgs(redSize == 8)
            if (alphaSize == 8) {
                return PixelFormat.RGBA_8888
            }
            if (alphaSize == 0) {
                return PixelFormat.RGB_888
            }
            throw IllegalArgumentException("违法参数")
        }

        if (redSize == 5 && greenSize == 6 && blueSize == 5 && alphaSize == 0) {
            return PixelFormat.RGB_565
        }
        throw IllegalArgumentException("违法参数")
    }


}
