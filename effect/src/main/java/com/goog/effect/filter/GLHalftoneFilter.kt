package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.utils.checkArgs
import com.goog.effect.utils.loadFilterFromAsset

///半色调滤镜
class GLHalftoneFilter(fraction: Float = 0.01f, aspectRatio: Float = 1f) : GLFilter() {
    private var fractionalWidthOfPixel = 0.01f
    private var aspectRatio = 1f

    init {
        setFractionalWidthOfAPixel(fraction)
        setAspectRatio(aspectRatio)
    }

    fun setAspectRatio(v: Float) {
        checkArgs(v > 0, "aspect ratio must be greater than 0")
        this.aspectRatio = v
    }

    fun setFractionalWidthOfAPixel(v: Float) {
        checkArgs(v in 0.0..1.0, "fractionalWidthOfPixel must in [0,1]")
        this.fractionalWidthOfPixel = v
    }

    override fun setFrameSize(width: Int, height: Int) {
        super.setFrameSize(width, height)
        ///TODO: 写反了?
        aspectRatio = height.toFloat() / width.toFloat()
    }

    override fun onDraw(fbo: FrameBufferObject?) {
        put("fractionalWidthOfPixel", fractionalWidthOfPixel)
        put("aspectRatio", aspectRatio)
    }

    override fun getFragmentShader(): String {
       return loadFilterFromAsset("filters/halftone.frag")
    }
}
