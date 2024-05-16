package com.goog.video.view.surface

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.goog.video.Player
import com.goog.video.filter.GLFilter
import com.goog.video.gl.ISurfaceView
import com.goog.video.gl.SimpleConfigChooser
import com.goog.video.gl.SimpleContextFactory
import com.goog.video.gl.SimpleRenderer

open class GLSurfaceView2 @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    GLSurfaceView(context, attrs), ISurfaceView {
    private val renderer: SimpleRenderer

    init {
        setEGLContextFactory(SimpleContextFactory())
        val chooser=SimpleConfigChooser()
        setEGLConfigChooser(chooser)
        setZOrderOnTop(true)
        holder.setFormat(chooser.getPixelFormat())
        renderer = SimpleRenderer(this)
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
