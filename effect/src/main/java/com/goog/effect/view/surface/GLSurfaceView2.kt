package com.goog.effect.view.surface

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.goog.effect.Player
import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.ISurfaceView
import com.goog.effect.gl.SimpleConfigChooser
import com.goog.effect.gl.SimpleContextFactory
import com.goog.effect.gl.FilterRenderer
import com.goog.effect.utils.ContextUtil

open class GLSurfaceView2 @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    GLSurfaceView(context, attrs), ISurfaceView {
    private val renderer: FilterRenderer

    init {
        ContextUtil.initContext(context)
        setEGLContextFactory(SimpleContextFactory())
        val chooser = SimpleConfigChooser.RGBA8888()
        setEGLConfigChooser(chooser)
        setZOrderOnTop(true)
        holder.setFormat(chooser.getPixelFormat())
        renderer = FilterRenderer(this)
        setRenderer(renderer)
    }

    open fun setPlayer(player: Player?) {
        renderer.setPlayer(player)
    }

    open fun setGlFilter(glFilter: GLFilter?) {
        renderer.setGlFilter(glFilter)
    }

    override fun onPause() {
        super.onPause()
        renderer.onPause()
    }
}
